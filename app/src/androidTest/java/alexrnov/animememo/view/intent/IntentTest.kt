package alexrnov.animememo.view.intent

import alexrnov.animememo.view.activity.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import alexrnov.animememo.R
import alexrnov.animememo.view.activity.AboutGameActivity
import alexrnov.animememo.view.activity.FavoritesActivity
import alexrnov.animememo.view.activity.GameActivity
import alexrnov.animememo.view.activity.SettingsActivity
import alexrnov.animememo.view.statistics.StatisticsActivity
import androidx.test.espresso.intent.Intents.intended
import org.hamcrest.CoreMatchers.allOf

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class IntentTest {
	@get:Rule
	val activityRule = ActivityScenarioRule(MainActivity::class.java)

	@Before
	fun setUp() = Intents.init()

	@After
	fun tearDown() = Intents.release()

	@Test
	fun gameIntent() {
		onView(withId(R.id.gameButton)).perform(click())
		intended(allOf(hasComponent(GameActivity::class.java.name)))
	}

	@Test
	fun statisticsIntent() {
		onView(withId(R.id.statisticsButton)).perform(click())
		intended(allOf(hasComponent(StatisticsActivity::class.java.name)))
	}

	@Test
	fun favoritesIntent() {
		onView(withId(R.id.favoritesButton)).perform(click())
		intended(allOf(hasComponent(FavoritesActivity::class.java.name)))
	}

	@Test
	fun aboutGameIntent() {
		onView(withId(R.id.aboutGameButton)).perform(click())
		intended(allOf(hasComponent(AboutGameActivity::class.java.name)))
	}

	@Test
	fun settingsIntent() {
		onView(withId(R.id.settingsButton)).perform(click())
		intended(allOf(hasComponent(SettingsActivity::class.java.name)))
	}
}