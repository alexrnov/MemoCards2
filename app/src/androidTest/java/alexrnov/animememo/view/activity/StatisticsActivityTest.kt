package alexrnov.animememo.view.activity

import alexrnov.animememo.R
import alexrnov.animememo.TestUtils.Companion.BASIC_SAMPLE_PACKAGE
import alexrnov.animememo.TestUtils.Companion.LAUNCH_TIMEOUT
import alexrnov.animememo.view.statistics.StatisticsActivity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class StatisticsActivityTest {
	private lateinit var device: UiDevice

	@get:Rule
	val activityRule = ActivityScenarioRule(StatisticsActivity::class.java)

	private lateinit var dateText: String
	private lateinit var cardsText: String
	private lateinit var errorsText: String

	@Before
	fun init() {
		// инициализировать здесь, чтобы не было несоответствий при тестировании на устройствах с другой локализацией
		activityRule.scenario.onActivity {
			dateText = it.getString(R.string.title_date)
			cardsText = it.getString(R.string.title_cards)
			errorsText = it.getString(R.string.title_errors)
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

		val statisticsButton: UiObject = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/statisticsButton"))
		statisticsButton.click()

		deviceWait()
	}

	@Test
	fun displayedComponents() {
		device.setOrientationNatural()
		onView(withId(R.id.clearStatisticsButton)).check(matches(isDisplayed()))
		isMatchesTexts()

		device.setOrientationLandscape()
		onView(withId(R.id.clearStatisticsButton)).check(matches(isDisplayed()))
		isMatchesTexts()
	}

	/* проверить значения текста для компонентов */
	private fun isMatchesTexts() {
		onView(withId(R.id.titleDate)).check(matches(withText(dateText)))
		onView(withId(R.id.titleCards)).check(matches(withText(cardsText)))
		onView(withId(R.id.titleErrors)).check(matches(withText(errorsText)))
	}

	private fun deviceWait(time: Long = LAUNCH_TIMEOUT) {
		device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), time)
	}
}
