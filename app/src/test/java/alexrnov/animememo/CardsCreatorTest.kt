package alexrnov.animememo

import alexrnov.animememo.cards.CardsCreator
import alexrnov.animememo.cards.SceneSettings
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CardsCreatorTest {
	val cardsCreator = CardsCreator()
	val context: Context = ApplicationProvider.getApplicationContext()

	@Test
	fun testResourceLoading() {
		val appName = context.getString(R.string.app_name)
		assertNotNull(appName)
		assertNotNull(context)
	}

	@Test
	fun createCards() {
		val cardsQuantity = 12

		val sceneSettings = mock(SceneSettings::class.java)
		`when`(sceneSettings.frontCardsSize).thenReturn(208)
		`when`(sceneSettings.backCardsSize).thenReturn(32)
		`when`(sceneSettings.material).thenReturn("pattern")
		`when`(sceneSettings.cardsQuantity).thenReturn(cardsQuantity)
		`when`(sceneSettings.rotationSpeed).thenReturn(6.0f)
		`when`(sceneSettings.backgroundColor).thenReturn("black")

		val cards = cardsCreator.createCards(context, 1.0f, sceneSettings)

		assertEquals(cardsQuantity, cards.size)
		cards.onEachIndexed { index, card ->
			assertEquals(index, card.key)
			assertFalse(card.value.isOpen())
			assertFalse(card.value.isRotationProcess())
			assertTrue(card.value.frontPath.contains("front"))
			assertTrue(card.value.frontPath.contains(".jpg"))
		}
	}

	@Test
	fun recoveryCards() {
		val cards = cardsCreator.recoveryCards(context, 1.0f)
		assertEquals(0, cards.size)
	}

	@Test
	fun createCardsFromDB() {
		val favoritesPaths = listOf(
			"front/89.jpg",
			"front/186.jpg",
			"front/146.jpg",
			"front/114.jpg"
		)

		val (cards, _) = cardsCreator.createCardsFromDB(context, 1.0f, favoritesPaths)

		assertEquals(6, cards.size)
		assertEquals(4, cards.filter { it.value.frontPath.contains("front") }.size)
		assertEquals(2, cards.filter { it.value.frontPath.contains("empty") }.size)

		cards.onEachIndexed { index, card ->
			assertEquals(index, card.key)
			assertFalse(card.value.isOpen())
			assertFalse(card.value.isRotationProcess())
			assertTrue(card.value.frontPath.contains(".jpg"))
		}
	}
}