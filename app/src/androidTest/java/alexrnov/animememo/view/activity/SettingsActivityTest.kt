package alexrnov.animememo.view.activity

import alexrnov.animememo.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class SettingsActivityTest {
	private lateinit var backgroundTitle: String
	private lateinit var blackText: String
	private lateinit var grayText: String
	private lateinit var whiteText: String

	private lateinit var materialTitle: String
	private lateinit var stoneText: String
	private lateinit var plasticText: String
	private lateinit var patternText: String

	private lateinit var cardsQuantityTitle: String
	private lateinit var lowCardsText: String
	private lateinit var mediumCardsText: String
	private lateinit var manyCardsText: String
	private lateinit var maxCardsText: String

	private lateinit var rotationSpeedTitle: String
	private lateinit var lowSpeedText: String
	private lateinit var mediumSpeedText: String
	private lateinit var fastSpeedText: String

	@get:Rule
	val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

	private lateinit var device: UiDevice

	@Before
	fun init() {
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

		activityRule.scenario.onActivity {
			backgroundTitle = it.getString(R.string.background_title)
			blackText = it.getString(R.string.black_color)
			grayText = it.getString(R.string.gray_color)
			whiteText = it.getString(R.string.white_color)

			materialTitle = it.getString(R.string.material_title)
			stoneText = it.getString(R.string.stone_material)
			plasticText = it.getString(R.string.plastic_material)
			patternText = it.getString(R.string.pattern_material)

			cardsQuantityTitle = it.getString(R.string.cards_title)
			lowCardsText = it.getString(R.string.cards12)
			mediumCardsText = it.getString(R.string.cards16)
			manyCardsText = it.getString(R.string.cards20)
			maxCardsText = it.getString(R.string.cards30)

			rotationSpeedTitle = it.getString(R.string.rotation_speed_title)
			lowSpeedText = it.getString(R.string.lowSpeed)
			mediumSpeedText = it.getString(R.string.mediumSpeed)
			fastSpeedText = it.getString(R.string.fastSpeed)
		}
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

	private fun isMatchesNames() {
		onView(withId(R.id.backgroundTextView)).check(matches(withText(backgroundTitle)))
		onView(withId(R.id.blackRadioButton)).check(matches(withText(blackText)))
		onView(withId(R.id.grayRadioButton)).check(matches(withText(grayText)))
		onView(withId(R.id.whiteRadioButton)).check(matches(withText(whiteText)))

		onView(withId(R.id.materialTextView)).check(matches(withText(materialTitle)))
		onView(withId(R.id.stoneRadioButton)).check(matches(withText(stoneText)))
		onView(withId(R.id.plasticRadioButton)).check(matches(withText(plasticText)))
		onView(withId(R.id.patternRadioButton)).check(matches(withText(patternText)))

		onView(withId(R.id.cardsQuantityTextView)).check(matches(withText(cardsQuantityTitle)))
		onView(withId(R.id.lowCardsRadioButton)).check(matches(withText(lowCardsText)))
		onView(withId(R.id.mediumCardsRadioButton)).check(matches(withText(mediumCardsText)))
		onView(withId(R.id.manyCardsRadioButton)).check(matches(withText(manyCardsText)))
		onView(withId(R.id.maxCardsRadioButton)).check(matches(withText(maxCardsText)))

		onView(withId(R.id.rotationSpeedTextView)).check(matches(withText(rotationSpeedTitle)))
		onView(withId(R.id.lowSpeedRadioButton)).check(matches(withText(lowSpeedText)))
		onView(withId(R.id.mediumSpeedRadioButton)).check(matches(withText(mediumSpeedText)))
		onView(withId(R.id.fastSpeedRadioButton)).check(matches(withText(fastSpeedText)))
	}

	private fun isDisplayedComponents() {
		onView(withId(R.id.backgroundTextView)).check(matches(isDisplayed()))
		onView(withId(R.id.blackRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.grayRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.whiteRadioButton)).check(matches(isDisplayed()))

		onView(withId(R.id.materialTextView)).check(matches(isDisplayed()))
		onView(withId(R.id.stoneRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.plasticRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.patternRadioButton)).check(matches(isDisplayed()))

		onView(withId(R.id.cardsQuantityTextView)).check(matches(isDisplayed()))
		onView(withId(R.id.lowCardsRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.mediumCardsRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.manyCardsRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.maxCardsRadioButton)).check(matches(isDisplayed()))

		onView(withId(R.id.rotationSpeedTextView)).check(matches(isDisplayed()))
		onView(withId(R.id.lowSpeedRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.mediumSpeedRadioButton)).check(matches(isDisplayed()))
		onView(withId(R.id.fastSpeedRadioButton)).check(matches(isDisplayed()))
	}
}