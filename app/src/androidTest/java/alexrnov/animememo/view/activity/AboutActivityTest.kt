package alexrnov.animememo.view.activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import alexrnov.animememo.R
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import alexrnov.animememo.TestUtils.Companion.BASIC_SAMPLE_PACKAGE
import alexrnov.animememo.TestUtils.Companion.LAUNCH_TIMEOUT


@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18) // аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
class AboutActivityTest {
	private lateinit var device: UiDevice

	private lateinit var rulesTitle: String
	private lateinit var rulesDescription: String
	private lateinit var favoritesDescription: String
	private lateinit var picturesTitle: String
	private lateinit var picturesDescription: String
	private lateinit var graphicsTitle: String
	private lateinit var graphicsDescription: String

	@get:Rule
	val activityRule = ActivityScenarioRule(AboutGameActivity::class.java)

	@Before
	fun startMainActivityFromHomeScreen() {
		// инициализировать здесь, чтобы не было несоответствий при тестировании на устройствах с другой локализацией
		activityRule.scenario.onActivity {
			rulesTitle = it.getString(R.string.rules_title)
			rulesDescription = it.getString(R.string.rules_description)
			favoritesDescription = it.getString(R.string.favorites_description)
			picturesTitle = it.getString(R.string.pictures_title)
			picturesDescription = it.getString(R.string.pictures_description)
			graphicsTitle = it.getString(R.string.graphics_title)
			graphicsDescription = it.getString(R.string.graphics_description)
		}

		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
		device.pressHome()

		// ждем запуска
		val launcherPackage: String = device.launcherPackageName
		assertThat(launcherPackage, CoreMatchers.notNullValue())
		device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT)

		// запустить приложение
		val context = ApplicationProvider.getApplicationContext<Context>()
		val intent = context.packageManager
			.getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)?.apply {
				// очистить все предыдущие instances
				addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
			}

		context.startActivity(intent)
		deviceWait()

		val aboutGameButton: UiObject = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/aboutGameButton"))
		aboutGameButton.click()

		deviceWait()
	}

	@Test
	fun displayedComponents() {
		device.setOrientationNatural()
		isDisplayedComponents()
		isMatchesTexts()

		device.setOrientationLandscape()
		isDisplayedComponents()
		isMatchesTexts()
	}

	/* проверить видимость компонентов */
	private fun isDisplayedComponents() {
		onView(withId(R.id.aboutGameAppBar)).check(matches(isDisplayed()))
		onView(withId(R.id.rulesTitle)).check(matches(isDisplayed()))
		onView(withId(R.id.rulesDescription)).check(matches(isDisplayed()))
		onView(withId(R.id.favoritesDescription)).check(matches(isDisplayed()))
		onView(withId(R.id.picturesTitle)).check(matches(isDisplayed()))
		onView(withId(R.id.picturesDescription)).check(matches(isDisplayed()))
		onView(withId(R.id.graphicsTitle)).check(matches(isDisplayed()))
		onView(withId(R.id.graphicsDescription)).check(matches(isDisplayed()))
	}

	/* проверить значения текста для компонентов */
	private fun isMatchesTexts() {
		onView(withId(R.id.rulesTitle)).check(matches(withText(rulesTitle)))
		onView(withId(R.id.rulesDescription)).check(matches(withText(rulesDescription)))
		onView(withId(R.id.favoritesDescription)).check(matches(withText(favoritesDescription)))
		onView(withId(R.id.picturesTitle)).check(matches(withText(picturesTitle)))
		onView(withId(R.id.picturesDescription)).check(matches(withText(picturesDescription)))
		onView(withId(R.id.graphicsTitle)).check(matches(withText(graphicsTitle)))
		onView(withId(R.id.graphicsDescription)).check(matches(withText(graphicsDescription)))
	}

	private fun deviceWait(time: Long = LAUNCH_TIMEOUT) {
		device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), time)
	}
}