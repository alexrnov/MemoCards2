package alexrnov.animememo.cards

import alexrnov.enginegl.Textures
import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.enginegl.Object3D
import android.content.Context
import androidx.core.content.edit
import kotlin.Int
import kotlin.random.Random

class CardsCreator {
	private val cardQuality: Int get() = appStorage.getInt("cardsQuantity", 12)
	private val cardPairs: Int get() = cardQuality / 2

	fun createCards(context: Context, scale: Float, sceneSettings: SceneSettings): Map<Int, Card> {
		val frontPictures = frontPictures(sceneSettings)

		val cardsAsPaths = if (sceneSettings.material == "pattern"
				|| sceneSettings.material == "plastic") {
			val backPicture = getBackPicture(sceneSettings)
			getCardsWithOneBackground(frontPictures, backPicture)
		} else {
			val backPictures = getBackPictures(sceneSettings)
			getCardsWithDifferentBackground(frontPictures, backPictures)
		}

		appStorage.edit { putStringSet("cards", cardsAsPaths) }

		val (cardsWithTextures, _) = getCardsWithTextures(context, cardsAsPaths, scale)
		return cardsWithTextures
	}

	fun recoveryCards(context: Context, scale: Float): Map<Int, Card> {
		val cards = appStorage.getStringSet("cards", emptySet())
		cards?.let {
			val (cardsWithTextures, _) = getCardsWithTextures(context, it, scale)
			return cardsWithTextures
		}
		return emptyMap()
	}

	private fun frontPictures(sceneSettings: SceneSettings): List<String> {
		val frontCardsSize = sceneSettings.frontCardsSize
		val frontNumbers = (1..frontCardsSize).shuffled(Random.Default).take(cardPairs)
		val frontPictures: MutableList<String> = mutableListOf()
		(0..cardPairs - 1).forEach {
			frontPictures.add("front/${frontNumbers[it]}.jpg")
		}
		return frontPictures
	}

	private fun getBackPicture(sceneSettings: SceneSettings): String {
		val material = sceneSettings.material
		val backCardsSize = sceneSettings.backCardsSize
		val backNumber = (1..backCardsSize).random()
		return "back/$material/${backNumber}.jpg"
	}

	private fun getCardsWithOneBackground(
		frontPictures: List<String>,
		backPicture: String
	): Set<String> {
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..cardPairs - 1) {
			cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
			cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
		}
		return cardsWithPaths
			.shuffled(Random.Default)
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}

	private fun getBackPictures(sceneSettings: SceneSettings): ArrayDeque<String> {
		val backCardsSize = sceneSettings.backCardsSize
		val material = sceneSettings.material
		val backNumbers = (1..backCardsSize).shuffled(Random.Default).take(cardQuality)
		val backPictures = ArrayDeque<String>()
		(0..backNumbers.size - 1).forEach {
			backPictures.add("back/$material/${backNumbers[it]}.jpg")
		}
		return backPictures
	}

	private fun getCardsWithDifferentBackground(
		frontPictures: List<String>,
		backPictures: ArrayDeque<String>
	): Set<String> {
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..cardPairs - 1) {
			cardsWithPaths.add("${i}:${frontPictures[i]}:${backPictures.removeFirst()}")
			cardsWithPaths.add("${i}:${frontPictures[i]}:${backPictures.removeFirst()}")
		}
		return cardsWithPaths
			.shuffled(Random.Default)
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}

	private fun getCardsWithTextures(
		context: Context,
		cardsWithPaths: Set<String>,
		scale: Float
	): Pair<Map<Int, Card>, IntArray> {
		val cardsWithTextures: MutableMap<Int, Card> = mutableMapOf()

		val textureIds = mutableListOf<Int>()
		cardsWithPaths.forEach { card ->
			val cardData = card.split(":")
			val frontTextureId = Textures.loadTextureWithMipMapFromAsset(context, cardData[2])
			val firstBackTextureId = Textures.loadTextureWithMipMapFromAsset(context, cardData[3])
			textureIds.add(frontTextureId)
			textureIds.add(firstBackTextureId)

			val card = createCard(context, cardData[1].toInt(), frontTextureId, firstBackTextureId, scale, cardData[2])
			cardsWithTextures.put(cardData[0].toInt(), card)
		}
		return Pair(cardsWithTextures, textureIds.toIntArray())
	}

	private fun createCard(context: Context, id: Int, pic: Int, backPic: Int, scale: Float, frontPath: String): Card {
		return Card(
			id,
			Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", pic, scale),
			Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", backPic, scale),
			frontPath
		)
	}

	/**
	 * Создать карты на основе данных БД (избранные карты)
	 */
	fun createCardsFromDB(context: Context, scale: Float, frontPaths: List<String>): Pair<Map<Int, Card>, IntArray> {
		val cardsAsPaths = getCardsFromDB(frontPaths)
		val (cardsWithTextures, texturesIds) = getCardsWithTextures(context, cardsAsPaths, scale)
		return Pair(cardsWithTextures, texturesIds)
	}

	private fun getCardsFromDB(frontPictures: List<String>, ): Set<String> {
		val backPicture = "back/pattern/1.jpg"
		val emptyPicture = "empty/1.jpg"
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..5) {
			if (i < frontPictures.size) {
				cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
			} else {
				cardsWithPaths.add("${i}:$emptyPicture:$backPicture")
			}
		}
		return cardsWithPaths
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}

	fun createLargeCardsFromDB(context: Context, scale: Float, frontPaths: List<String>): Pair<Map<Int, Card>, IntArray> {
		val cardsAsPaths = getLargeCardsFromDB(frontPaths)
		val (cardsWithTextures, textureIds) = getCardsWithTextures(context, cardsAsPaths, scale)
		return Pair(cardsWithTextures, textureIds)
	}

	private fun getLargeCardsFromDB(frontPictures: List<String>, ): Set<String> {
		val backPicture = "back/pattern/1.jpg"
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..frontPictures.size - 1) {
			cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
		}
		return cardsWithPaths
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}
}