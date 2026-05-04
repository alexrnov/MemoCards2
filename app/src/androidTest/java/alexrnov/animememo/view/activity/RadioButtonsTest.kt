package alexrnov.animememo.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Test
import alexrnov.animememo.R
import alexrnov.animememo.TestUtils.Companion.BASIC_SAMPLE_PACKAGE
import alexrnov.animememo.TestUtils.Companion.LAUNCH_TIMEOUT
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RadioButtonsTest {
	private lateinit var device: UiDevice

	@Before
	fun init() {
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
		device.pressHome()

		// сбросить кеш
		InstrumentationRegistry.getInstrumentation()
			.uiAutomation
			.executeShellCommand("adb shell pm clear $BASIC_SAMPLE_PACKAGE")

		deviceWait()

		// ждем запуска
		val launcherPackage: String = device.launcherPackageName
		assertThat(launcherPackage, CoreMatchers.notNullValue())
		device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT)

		// запустить приложение
		val context = ApplicationProvider.getApplicationContext<Context>()
		val intent = context.packageManager
			.getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)?.apply {
				// очистить все предыдузие instances
				addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
			}

		context.startActivity(intent)
		deviceWait()

		val aboutGameButton: UiObject = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/settingsButton"))
		aboutGameButton.click()

		deviceWait()
	}

	@Test
	fun test1_radioButtons() {
		device.setOrientationNatural()
		isRadioButtonMatches()
		device.setOrientationLandscape()
		isRadioButtonMatches()
	}

	@Test
	fun test2_radioButtons() {
		onView(withId(R.id.blackRadioButton)).perform(click())
		deviceWait()

		onView(withId(R.id.blackRadioButton)).check(matches(isChecked()))
		onView(withId(R.id.grayRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.whiteRadioButton)).check(matches(isNotChecked()))

		onView(withId(R.id.whiteRadioButton)).perform(click())
		deviceWait()

		onView(withId(R.id.blackRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.grayRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.whiteRadioButton)).check(matches(isChecked()))
	}

	private fun isRadioButtonMatches() {
		onView(withId(R.id.grayRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.blackRadioButton)).check(matches(isChecked())) // отмечено по умолчанию
		onView(withId(R.id.whiteRadioButton)).check(matches(isNotChecked()))

		onView(withId(R.id.stoneRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.plasticRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.patternRadioButton)).check(matches(isChecked()))

		onView(withId(R.id.lowCardsRadioButton)).check(matches(isChecked()))
		onView(withId(R.id.mediumCardsRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.manyCardsRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.maxCardsRadioButton)).check(matches(isNotChecked()))

		onView(withId(R.id.lowSpeedRadioButton)).check(matches(isNotChecked()))
		onView(withId(R.id.mediumSpeedRadioButton)).check(matches(isChecked()))
		onView(withId(R.id.fastSpeedRadioButton)).check(matches(isNotChecked()))
	}

	private fun deviceWait(time: Long = LAUNCH_TIMEOUT) {
		device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), time)
	}
}