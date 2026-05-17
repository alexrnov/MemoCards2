package alexrnov.animememo.view.activity

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Before
import alexrnov.animememo.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import androidx.test.uiautomator.*
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18) // аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
class MainActivityTest {
	private lateinit var startGame: String
	private lateinit var statistics: String
	private lateinit var favorites: String
	private lateinit var aboutGame: String
	private lateinit var settings: String

	@get:Rule
	val activityRule = ActivityScenarioRule(MainActivity::class.java)

	private lateinit var device: UiDevice

	@Before
	fun init() {
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
		// инициализировать здесь, чтобы не было несоответствий при тестировании на устройствах с другой локализацией
		activityRule.scenario.onActivity {
			startGame = it.getString(R.string.start_game)
			statistics = it.getString(R.string.statistics)
			favorites = it.getString(R.string.favorites)
			aboutGame = it.getString(R.string.about_game)
			settings = it.getString(R.string.settings)
		}
	}

	@Test
	fun packageName() {
		val appContext = InstrumentationRegistry.getInstrumentation().targetContext
		assertEquals("alexrnov.animememo", appContext.packageName)
	}

	@Test
	fun displayedComponents() {
		device.setOrientationNatural()
		isDisplayedComponents()
		isMatchesNames()

		device.setOrientationLandscape()
		isDisplayedComponents()
		isMatchesNames()
	}

	/* проверить видимость компонентов */
	private fun isDisplayedComponents() {
		onView(withId(R.id.mainAppBar)).check(matches(isDisplayed()))
		onView(withId(R.id.gameButton)).check(matches(isDisplayed()))
		onView(withId(R.id.statisticsButton)).check(matches(isDisplayed()))
		onView(withId(R.id.favoritesButton)).check(matches(isDisplayed()))
		onView(withId(R.id.aboutGameButton)).check(matches(isDisplayed()))
		onView(withId(R.id.settingsButton)).check(matches(isDisplayed()))
	}

	/* проверить значения текста для компонентов основного меню */
	private fun isMatchesNames() {
		onView(withId(R.id.gameButton)).check(matches(withText(startGame)))
		onView(withId(R.id.statisticsButton)).check(matches(withText(statistics)))
		onView(withId(R.id.favoritesButton)).check(matches(withText(favorites)))
		onView(withId(R.id.aboutGameButton)).check(matches(withText(aboutGame)))
		onView(withId(R.id.settingsButton)).check(matches(withText(settings)))
	}
}