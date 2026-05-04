package alexrnov.animememo.render.favorites

import alexrnov.animememo.Initialization.FAVORITES_DB
import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.view.activity.FavoritesActivity
import alexrnov.animememo.cards.Card
import alexrnov.animememo.cards.CardsCreator
import alexrnov.animememo.cards.setComposition
import alexrnov.animememo.cards.setCompositionForLargeCards
import alexrnov.animememo.database.favorites.FavoritesDatabase
import alexrnov.animememo.render.setBackgroundColor
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.core.content.edit
import androidx.room.Room.databaseBuilder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sin

class FavoritesRenderer(private val context: Context) : GLSurfaceView.Renderer {
	private var favoritesActivity: FavoritesActivity? = null
	private var ky = 0.30f // coefficient for camera rotation

	private val viewMatrix = FloatArray(16)
	private val projectionMatrix = FloatArray(16)

	private var cards: Map<Int, Card> = mapOf()
	private var largeCards: Map<Int, Card> = mapOf()

	private val rotationCameraRadius = 2.2f

	private var zCamera = 3.0f
	private val scale = 1.0f

	private var reset = false

	private var largeCard: Card? = null

	var screenWidth: Int = 0
	var screenHeight: Int = 0

	private var isPortrait = true

	private lateinit var textureIds: IntArray
	private lateinit var largeTextureIds: IntArray

	private lateinit var favoritesDatabase: FavoritesDatabase

	override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
		favoritesDatabase = databaseBuilder(
			context,
			FavoritesDatabase::class.java, FAVORITES_DB
		).allowMainThreadQueries().build()

		val requests = favoritesDatabase.requests()
		val favorites = requests.all
		val favoritesPaths = favorites.mapNotNull { it.path }
		val cardsCreator = CardsCreator()

		val (cardsFromDB, textures) = cardsCreator.createCardsFromDB(context, scale, favoritesPaths)
		cards = cardsFromDB
		textureIds = textures

		val (largeCardsFromDB, largeTextures) = cardsCreator.createLargeCardsFromDB(context, scale * 1.7f, favoritesPaths)
		largeCards = largeCardsFromDB
		largeTextureIds = largeTextures

		cameraPosition(-350.0f)

		val largeCardIndex = appStorage.getInt("largeCardIndex", -1)
		if (largeCardIndex != -1) {
			largeCard = largeCards[largeCardIndex]
		}

		val color = appStorage.getString("backgroundColor", "black") ?: "black"
		setBackgroundColor(color)

		GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FASTEST)
	}

	override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
		isPortrait = width < height
		GLES20.glViewport(0, 0, width, height) // set screen size
		this.screenWidth = width
		this.screenHeight = height

		val aspect = width.toFloat() / height.toFloat()
		val k = 1f / 30 // coefficient is selected empirically

		if (isPortrait) { // portrait orientation
			Matrix.frustumM(projectionMatrix, 0, -1f * k, 1f * k, (1 / -aspect) * k, (1 / aspect) * k, 0.1f, 40f)
		} else { // landscape orientation
			Matrix.frustumM(projectionMatrix, 0, -aspect * k, aspect * k, -1f * k, 1f * k, 0.1f, 40f)
		}

		calibrateCamera(width, height)
		setComposition(cards, portrait = isPortrait)
		setCompositionForLargeCards(largeCards, portrait = isPortrait)
		openCards()
	}

	override fun onDrawFrame(gl: GL10?) {
		if (reset) {
			val requests = favoritesDatabase.requests()
			val favorites = requests.all

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

			// удалить ранее созданные текстуры
			GLES20.glDeleteTextures(textureIds.size, textureIds, 0)
			// удалить ранее созданные текстуры для больших карточек
			GLES20.glDeleteTextures(largeTextureIds.size, largeTextureIds, 0)

			val favoritesPaths = favorites.mapNotNull { it.path }
			val cardsCreator = CardsCreator()

			val (cardsFromDB, _) = cardsCreator.createCardsFromDB(context, scale, favoritesPaths)
			cards = cardsFromDB

			val (largeCardsFromDB, _) = cardsCreator.createLargeCardsFromDB(context, scale * 1.7f, favoritesPaths)
			largeCards = largeCardsFromDB

			setComposition(cards, portrait = isPortrait)
			setCompositionForLargeCards(largeCards, portrait = isPortrait)
			openCards()
			reset = false
		}

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
		GLES20.glEnable(GLES20.GL_DEPTH_TEST) // enable depth test

		GLES20.glEnable(GLES20.GL_CULL_FACE) // разрешить отбрасывание
		GLES20.glCullFace(GLES20.GL_BACK) // отбрасывать заднюю грань примитивов при рендеринге

		cards.forEach { index, card ->
			card.draw(viewMatrix, projectionMatrix)
		}

		largeCard?.draw(viewMatrix, projectionMatrix)
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
		if (reset) return
		if (largeCard != null) {
			val (index, _) = getSelectLargeCard(x, y)
			if (index == null) {
				appStorage.edit {
					putInt("largeCardIndex", -1)
				}
				largeCard = null
			}
		} else {
			val (index, _) = getSelectCard(x, y)
			if (index == null) {
				return
			}
			if (index < largeCards.size) {
				appStorage.edit {
					putInt("largeCardIndex", index)
				}
				largeCard = largeCards[index]
			}
		}
	}

	@Synchronized
	fun removeFavoriteCard(x: Float, y: Float) {
		if (largeCard != null) return

		val (_, card) = getSelectCard(x, y)

		val path = card?.frontPath ?: return
		if (!path.contains("empty")) {
			favoritesActivity?.openConfirmRemoveCardDialog(path)
		}
	}

	private fun getSelectCard(x: Float, y: Float): Pair<Int?, Card?>  {
		val xPass = x
		val yPass = screenHeight - y

		var index: Int? = null
		var card: Card? = null

		cards.forEach { currentIndex, currentCard ->
			val vertices = currentCard.getVertices(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f)
			
			val yMin = vertices.yMin
			val yMax = vertices.yMax
			val	xMin = vertices.xMax
			val	xMax = vertices.xMin

			if (xPass >= xMin && xPass <= xMax && yPass >= yMin && yPass <= yMax) {
				index = currentIndex
				card = currentCard
				return@forEach
			}
		}
		return Pair(index, card)
	}

	private fun getSelectLargeCard(x: Float, y: Float): Pair<Int?, Card?> {
		val xPass = x
		val yPass = screenHeight - y

		var index: Int? = null
		var card: Card? = null

		largeCards.forEach { currentIndex, currentCard ->
			val vertices = currentCard.getVertices(projectionMatrix, screenWidth, screenHeight, scale * 1.7f, 0.500000f, 0.888800f, 0.001000f)

			val yMin = vertices.yMin
			val yMax = vertices.yMax
			val xMin = vertices.xMax
			val xMax = vertices.xMin

			if (xPass >= xMin && xPass <= xMax && yPass >= yMin && yPass <= yMax) {
				index = currentIndex
				card = currentCard
				return@forEach
			}
		}
		return Pair(index, card)
	}

	private fun calibrateCamera(screenWidth: Int, screenHeight: Int) {
		val card = cards.getValue(0)
		val (cardsByWidth, cardsByHeight) = if (screenWidth < screenHeight) Pair(3f, 4.5f) else Pair(6.5f, 2.3f)
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

	private fun openCards() {
		cards.forEach { it.value.openCard() }
		largeCards.forEach { it.value.openCard() }
	}

	fun setFavoritesActivity(favoritesActivity: FavoritesActivity) {
		this.favoritesActivity = favoritesActivity
	}

	fun update() {
		reset = true
	}

	fun isOpenLargeCard(): Boolean {
		return largeCard != null
	}

	fun closeLargeCard() {
		largeCard = null
	}
}