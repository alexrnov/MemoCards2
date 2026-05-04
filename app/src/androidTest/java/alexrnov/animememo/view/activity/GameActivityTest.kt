package alexrnov.animememo.view.activity

import alexrnov.animememo.R
import alexrnov.animememo.TestUtils.Companion.BASIC_SAMPLE_PACKAGE
import alexrnov.animememo.TestUtils.Companion.LAUNCH_TIMEOUT
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import org.hamcrest.Matchers.not
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
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GameActivityTest {
	private lateinit var device: UiDevice

	private lateinit var gameButton: UiObject
	private lateinit var cancelButton: UiObject
	private lateinit var exitButton: UiObject

	@Before
	fun init() {
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
		device.pressHome()

		gameButton = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/gameButton"))
		cancelButton = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/button_repeat_game"))
		exitButton = device.findObject(UiSelector()
			.resourceId("${BASIC_SAMPLE_PACKAGE}:id/button_back_to_main_menu"))

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
		gameButton.click()
		deviceWait()
	}

	@Test
	fun test1_displayedDialog() {
		onView(withId(R.id.exitDialog)).check(matches(not(isDisplayed())))
		device.pressBack()

		deviceWait()
		onView(withId(R.id.exitDialog)).check(matches(isDisplayed()))

		cancelButton.click()
		deviceWait()
		onView(withId(R.id.exitDialog)).check(matches(not(isDisplayed())))

		device.pressBack()
		deviceWait()
		onView(withId(R.id.exitDialog)).check(matches(isDisplayed()))
		device.pressBack()
		onView(withId(R.id.exitDialog)).check(matches(not(isDisplayed())))
		device.pressBack()
		onView(withId(R.id.exitDialog)).check(matches(isDisplayed()))

		exitButton.click()
		deviceWait()

		// выход в меню
		onView(withId(R.id.gameButton)).check(matches(isDisplayed()))
	}

	@Test
	fun test2_orientationDialog() {
		device.setOrientationNatural()
		deviceWait()
		device.pressBack()
		onView(withId(R.id.exitDialog)).check(matches(isDisplayed()))
		device.setOrientationLandscape()
		deviceWait()
		onView(withId(R.id.exitDialog)).check(matches(isDisplayed()))

		exitButton.click()
		deviceWait()
		onView(withId(R.id.gameButton)).check(matches(isDisplayed()))
	}

	private fun deviceWait(time: Long = LAUNCH_TIMEOUT) {
		device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), time)
	}
}