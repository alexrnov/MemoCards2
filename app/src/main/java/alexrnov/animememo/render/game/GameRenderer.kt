package alexrnov.animememo.render.game

import alexrnov.enginegl.MeanValue
import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.view.activity.GameActivity
import alexrnov.animememo.cards.Card
import alexrnov.animememo.cards.CardsCreator
import alexrnov.animememo.cards.SceneSettings
import alexrnov.animememo.cards.setComposition
import alexrnov.animememo.render.setBackgroundColor
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import androidx.core.content.edit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sin

class GameRenderer(private val context: Context, private val sceneSettings: SceneSettings) : GLSurfaceView.Renderer {
	private var gameActivity: GameActivity? = null
	private var ky = 0.30f // coefficient for camera rotation

	private val viewMatrix = FloatArray(16)
	private val projectionMatrix = FloatArray(16)

	private var f = 0.0f

	private var smoothedDeltaRealTime_ms = 16.0f // initial value, Optionally you can save the new computed value (will change with each hardware) in Preferences to optimize the first drawing frames
	private var movAverageDeltaTime_ms = smoothedDeltaRealTime_ms // mov Average start with default value
	private var lastRealTimeMeasurement_ms: Long = 0 // temporal storage for last time measurement
	private val meanValue = MeanValue(5000.toShort())

	private var totalVirtualRealTime_ms = 0f
	private val speedAdjustments_ms = 0f // to introduce a virtual Time for the animation (reduce or increase animation speed)
	private var totalAnimationTime_ms = 0f
	private val fixedStepAnimation_ms = 20f // 20ms for a 50FPS descriptive animation
	private var interpolationRatio = 0f
	// smooth constant elements to play with
	private val movAveragePeriod = 5f // #frames involved in average calc (suggested values 5-100)
	private val smoothFactor = 0.1f // adjusting ratio (suggested values 0.01-0.5)


	private var delta = 0f

	private var cards: Map<Int, Card> = mapOf()

	private val rotationCameraRadius = 2.2f

	private var zCamera = 3.0f
	private val scale = 1.0f

	var screenWidth: Int = 0
	var screenHeight: Int = 0

	override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
		val newGame = appStorage.getBoolean("newGame", true)
		val cardsCreator = CardsCreator()
		if (newGame) {
			appStorage.edit { putBoolean("newGame", false) }
			cards = cardsCreator.createCards(context, scale, sceneSettings)
		} else {
			cards = cardsCreator.recoveryCards(context, scale)
		}

		cameraPosition(-350.0f)

		val color = sceneSettings.backgroundColor
		setBackgroundColor(color)

		GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FASTEST)
	}

	override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
		GLES20.glViewport(0, 0, width, height) // set screen size
		this.screenWidth = width
		this.screenHeight = height

		val aspect = width.toFloat() / height.toFloat()
		val k = 1f / 30 // coefficient is selected empirically

		if (width < height) { // portrait orientation
			Matrix.frustumM(projectionMatrix, 0, -1f * k, 1f * k, (1 / -aspect) * k, (1 / aspect) * k, 0.1f, 40f)
		} else { // landscape orientation
			Matrix.frustumM(projectionMatrix, 0, -aspect * k, aspect * k, -1f * k, 1f * k, 0.1f, 40f)
		}

		calibrateCamera(width, height)
		setComposition(cards, portrait = width < height)

		val secondCardIndex = appStorage.getInt("secondCardIndex", -1)
		val firstCardIndex = appStorage.getInt("firstCardIndex", -1)
		val firstCardId = appStorage.getInt("firstCardId", -1)
		val openCards = appStorage.getStringSet("openCards", emptySet<String>())
		openCards?.forEach {
			cards[it.toInt()]?.openCard()
		}
		if (firstCardIndex != -1) { // если открыта первая карта открыть ее при повороте экрана
			cards[firstCardIndex]?.openCard()
		}
		if (secondCardIndex != -1) {
			rotateSecondCard(firstCardId, firstCardIndex, secondCardIndex)
		}
	}

	override fun onDrawFrame(gl: GL10?) {
		delta = meanValue.add(interpolationRatio)
		f = f + delta * 2
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
		GLES20.glEnable(GLES20.GL_DEPTH_TEST) // enable depth test

		GLES20.glEnable(GLES20.GL_CULL_FACE) // разрешить отбрасывание
		GLES20.glCullFace(GLES20.GL_BACK) // отбрасывать заднюю грань примитивов при рендеринге

		cards.forEach { index, card ->
			if (card.isRotationProcess()) {
				card.rotate(delta, sceneSettings.rotationSpeed)
			}
			card.draw(viewMatrix, projectionMatrix)
		}

		defineDeltaTime()
	}

	@Synchronized
	fun cameraPosition(yDistance: Float) {
		if ((!(ky < -0.5) || !(yDistance < 0.0)) && (!(ky > 0.5) || !(yDistance >= 0.0))) {
			ky = ky + yDistance * 0.001f
		}

		val yCamera = (rotationCameraRadius * sin(ky.toDouble())).toFloat()

		Matrix.setLookAtM(viewMatrix, 0, 0.0f, -yCamera, zCamera, 0f, 0.0f, 0f, 0f, 1.0f, 0.0f)
	}

	@Synchronized
	fun openCard(x: Float, y: Float) {
		val secondCardIndex = appStorage.getInt("secondCardIndex", -1)
		if (secondCardIndex != -1) {
			// в настоящий момент открывается вторая карта
			return
		}

		val (index, card) = getSelectCard(x, y)
		if (index == null || card == null) {
			return
		}

		val openCards = appStorage.getStringSet("openCards", emptySet())
		if (openCards == null) {
			return
		}

		val firstCardIndex = appStorage.getInt("firstCardIndex", -1)
		// если нажата первая открытая карточка или карточка уже открытой пары
		if (firstCardIndex == index || openCards.contains(index.toString())) {
			return
		}

		val firstCardId = appStorage.getInt("firstCardId", -1)
		if (firstCardId == -1) { // если нажата первая карточка
			appStorage.edit {
				putInt("firstCardId", card.id)
				putInt("firstCardIndex", index)
			}
			card.setRotationProcess(true)
		} else { // если нажата вторая карточка
			appStorage.edit {
				putInt("secondCardIndex", index)
			}
			rotateSecondCard(firstCardId, firstCardIndex, index)
		}
	}

	@OptIn(DelicateCoroutinesApi::class)
	private fun rotateSecondCard(firstCardId: Int, firstCardIndex: Int, secondCardIndex: Int) {
		val secondCard = cards[secondCardIndex]?: return

		secondCard.setRotationProcess(true) {
			if (firstCardId == secondCard.id) { // если вторая карточка совпала с первой - оставить их открытыми
				val currentOpenCards = mutableSetOf(firstCardIndex.toString(), secondCardIndex.toString())
				val openCards = appStorage.getStringSet("openCards", emptySet<String>())
				openCards?.let { currentOpenCards.addAll(it) }
				appStorage.edit {
					putStringSet("openCards", currentOpenCards)
				}
				val cardsQuantity = appStorage.getInt("cardsQuantity", 12)
				val errors = appStorage.getInt("errors", 0)
				if (currentOpenCards.size == cardsQuantity) {
					GlobalScope.launch(Dispatchers.Main) {
						gameActivity?.finishGame(errors)
					}
				}
			} else { // если карточки не совпали - закрыть обе карты
				cards[firstCardIndex]?.setRotationProcess(true)
				secondCard.setRotationProcess(true)
				val errors = appStorage.getInt("errors", 0) + 1
				appStorage.edit {
					putInt("errors", errors)
				}
			}
			appStorage.edit {
				putInt("firstCardId", -1)
				putInt("firstCardIndex", -1)
				putInt("secondCardIndex", -1)
			}
		}
	}

	@OptIn(DelicateCoroutinesApi::class)
	@Synchronized
	fun addCardToFavorites(x: Float, y: Float) {
		val openCards = appStorage.getStringSet("openCards", emptySet())
		if (openCards == null) {
			return
		}

		val (index, _) = getSelectCard(x, y)
		if (openCards.contains(index.toString())) {
			val cards = appStorage.getStringSet("cards", emptySet())
			val card = cards?.firstOrNull {
				(index == it.split(":")[0].toInt())
			}
			card?.let {
				GlobalScope.launch(Dispatchers.Main) {
					val messageType = gameActivity?.addCardToFavorites(it.split(":")[2])
					if (messageType != null) {
						gameActivity?.showSnackBar(messageType)
					}
				}
			}
		}
	}

	private fun getSelectCard(x: Float, y: Float): Pair<Int?, Card?>  {
		val xPass = x
		val yPass = screenHeight - y

		var index: Int? = null
		var card: Card? = null

		cards.forEach { currentIndex, currentCard ->
			val vertices = currentCard.getVertices(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f)
			val xMin: Float
			val xMax: Float
			val yMin = vertices.yMin
			val yMax = vertices.yMax
			if (!currentCard.isOpen()) {
				xMin = vertices.xMin
				xMax = vertices.xMax
			} else {
				xMin = vertices.xMax
				xMax = vertices.xMin
			}
			if (xPass >= xMin && xPass <= xMax && yPass >= yMin
					&& yPass <= yMax && !currentCard.isRotationProcess()) {
				index = currentIndex
				card = currentCard
				return@forEach
			}
		}
		return Pair(index, card)
	}

	private fun calibrateCamera(screenWidth: Int, screenHeight: Int) {
		val card = cards.getValue(0)
		val (cardsByWidth, cardsByHeight) = cardsByScreen(cards.size, screenWidth < screenHeight)

		card.position(0.0f, 0.0f, 0.0f, 45.0f)
		var isScale = false
		var cardWidth: Float
		var cardHeight: Float

		while (!isScale) {
			card.defineView(viewMatrix, projectionMatrix)
			val objectSize = card.getSize(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f)
			cardWidth = objectSize.width
			cardHeight = objectSize.height

			if (screenWidth / cardWidth > cardsByWidth
				&& screenHeight / cardHeight > cardsByHeight
			) {
				isScale = true
			} else {
				zCamera = zCamera + 0.1f
				cameraPosition(0f)
			}
		}
	}

	private fun cardsByScreen(cardsQuantity: Int, isPortrait: Boolean): Pair<Float, Float> {
		return when (cardsQuantity) {
			12 -> if (isPortrait) Pair(3f, 4.5f) else Pair(6.5f, 2.3f)
			16 -> if (isPortrait) Pair(4.5f, 4.5f) else Pair(8.5f, 2.3f)
			20 -> if (isPortrait) Pair(4.5f, 5.5f) else Pair(11.2f, 2.3f)
			30 -> if (isPortrait) Pair(5.5f, 6.5f) else Pair(11.2f, 3.4f)
			else -> if (isPortrait) Pair(3f, 4.5f) else Pair(6.5f, 2.3f)
		}
	}

	fun setGameActivity(gameActivity: GameActivity) {
		this.gameActivity = gameActivity
	}

	private fun defineDeltaTime() {
		totalVirtualRealTime_ms += smoothedDeltaRealTime_ms + speedAdjustments_ms
		while (totalVirtualRealTime_ms > totalAnimationTime_ms) {
			totalAnimationTime_ms += fixedStepAnimation_ms
		}

		interpolationRatio = ((totalAnimationTime_ms - totalVirtualRealTime_ms)
				/ fixedStepAnimation_ms)

		val currTimePick_ms = SystemClock.uptimeMillis()
		val realTimeElapsed_ms: Float
		if (lastRealTimeMeasurement_ms > 0) {
			realTimeElapsed_ms = (currTimePick_ms - lastRealTimeMeasurement_ms).toFloat()
		} else {
			realTimeElapsed_ms = smoothedDeltaRealTime_ms // just the first time
		}
		movAverageDeltaTime_ms = (realTimeElapsed_ms + movAverageDeltaTime_ms
				* (movAveragePeriod - 1)) / movAveragePeriod

		// Calc a better approximation for smooth stepTime
		smoothedDeltaRealTime_ms = smoothedDeltaRealTime_ms +
				(movAverageDeltaTime_ms - smoothedDeltaRealTime_ms) * smoothFactor

		lastRealTimeMeasurement_ms = currTimePick_ms
	}
}