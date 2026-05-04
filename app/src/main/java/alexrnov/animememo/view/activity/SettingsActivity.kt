package alexrnov.animememo.view.activity

import alexrnov.animememo.Initialization.appStorage
import alexrnov.animememo.R
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit
import alexrnov.animememo.databinding.ActivitySettingsBinding
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
	private lateinit var blackRadioButton: RadioButton
	private lateinit var whiteRadioButton : RadioButton
	private lateinit var grayRadioButton: RadioButton
	private lateinit var backgroundColorRadioGroup: RadioGroup

	private lateinit var stoneRadioButton: RadioButton
	private lateinit var plasticRadioButton: RadioButton
	private lateinit var patternRadioButton: RadioButton
	private lateinit var materialRadioGroup: RadioGroup

	private lateinit var cardsQuantityRadioGroup: RadioGroup
	private lateinit var lowCardsRadioButton: RadioButton
	private lateinit var mediumCardsRadioButton: RadioButton
	private lateinit var manyCardsRadioButton: RadioButton
	private lateinit var maxCardsRadioButton: RadioButton

	private lateinit var rotationSpeedRadioGroup: RadioGroup
	private lateinit var lowSpeedRadioButton: RadioButton
	private lateinit var mediumSpeedRadioButton: RadioButton
	private lateinit var fastSpeedRadioButton: RadioButton

	private var binding: ActivitySettingsBinding? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		binding = ActivitySettingsBinding.inflate(layoutInflater)
		setContentView(binding?.root)

		val toolbar = findViewById<MaterialToolbar>(R.id.settingsAppBar)
		setSupportActionBar(toolbar)

		supportActionBar?.let {
			it.setDisplayHomeAsUpEnabled(true)
			it.setDisplayShowHomeEnabled(true)
		}

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsContainer)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		blackRadioButton = findViewById(R.id.blackRadioButton)
		whiteRadioButton = findViewById(R.id.whiteRadioButton)
		grayRadioButton = findViewById(R.id.grayRadioButton)
		backgroundColorRadioGroup = findViewById(R.id.backgroundColorRadioGroup)

		stoneRadioButton = findViewById(R.id.stoneRadioButton)
		plasticRadioButton = findViewById(R.id.plasticRadioButton)
		patternRadioButton = findViewById(R.id.patternRadioButton)
		materialRadioGroup = findViewById(R.id.materialRadioGroup)

		cardsQuantityRadioGroup = findViewById(R.id.cardsQuantityRadioGroup)
		lowCardsRadioButton = findViewById(R.id.lowCardsRadioButton)
		mediumCardsRadioButton = findViewById(R.id.mediumCardsRadioButton)
		manyCardsRadioButton = findViewById(R.id.manyCardsRadioButton)
		maxCardsRadioButton = findViewById(R.id.maxCardsRadioButton)

		rotationSpeedRadioGroup = findViewById(R.id.rotationSpeedRadioGroup)
		lowSpeedRadioButton = findViewById(R.id.lowSpeedRadioButton)
		mediumSpeedRadioButton = findViewById(R.id.mediumSpeedRadioButton)
		fastSpeedRadioButton = findViewById(R.id.fastSpeedRadioButton)

		defineBackgroundColorRadioButtons()
		defineMaterialRadioButtons()
		defineCardsQuantityRadioButtons()
		defineRotationSpeedRadioButtons()
		addListeners()
	}

	private fun addListeners() {
		backgroundColorRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val backgroundColor = when (checkedId) {
				blackRadioButton.id -> "black"
				whiteRadioButton.id -> "white"
				else -> "gray"
			}
			saveToStorage("backgroundColor", backgroundColor)
		}

		materialRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val material = when (checkedId) {
				stoneRadioButton.id -> "stone"
				plasticRadioButton.id -> "plastic"
				else -> "pattern"
			}
			saveToStorage("material", material)
		}

		cardsQuantityRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val cardsQuantity = when (checkedId) {
				lowCardsRadioButton.id -> 12
				mediumCardsRadioButton.id -> 16
				manyCardsRadioButton.id -> 20
				maxCardsRadioButton.id -> 30
				else -> 12
			}
			appStorage.edit {
				putInt("cardsQuantity", cardsQuantity)
			}
		}

		rotationSpeedRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val rotationSpeed = when (checkedId) {
				lowSpeedRadioButton.id -> 7f
				mediumSpeedRadioButton.id -> 10f
				else -> 14f
			}
			appStorage.edit {
				putFloat("rotationSpeed", rotationSpeed)
			}
		}
	}

	private fun saveToStorage(key: String, value: String) {
		appStorage.edit {
			putString(key, value)
		}
	}

	private fun defineBackgroundColorRadioButtons() {
		val currentValue = appStorage.getString("backgroundColor", "black") ?: "black"
		when (currentValue) {
			"black" -> backgroundColorRadioGroup.check(blackRadioButton.id)
			"white" -> backgroundColorRadioGroup.check(whiteRadioButton.id)
			"gray" -> backgroundColorRadioGroup.check(grayRadioButton.id)
		}
	}

	private fun defineMaterialRadioButtons() {
		val currentValue = appStorage.getString("material", "pattern") ?: "pattern"
		when (currentValue) {
			"stone" -> materialRadioGroup.check(stoneRadioButton.id)
			"plastic" -> materialRadioGroup.check(plasticRadioButton.id)
			"pattern" -> materialRadioGroup.check(patternRadioButton.id)
		}
	}

	private fun  defineCardsQuantityRadioButtons() {
		val currentValue = appStorage.getInt("cardsQuantity", 12)
		when (currentValue) {
			12 -> cardsQuantityRadioGroup.check(lowCardsRadioButton.id)
			16 -> cardsQuantityRadioGroup.check(mediumCardsRadioButton.id)
			20 -> cardsQuantityRadioGroup.check(manyCardsRadioButton.id)
			30 -> cardsQuantityRadioGroup.check(maxCardsRadioButton.id)
		}
	}

	private fun defineRotationSpeedRadioButtons() {
		val currentValue = appStorage.getFloat("rotationSpeed", 10f)
		when (currentValue) {
			7f -> rotationSpeedRadioGroup.check(lowSpeedRadioButton.id)
			10f -> rotationSpeedRadioGroup.check(mediumSpeedRadioButton.id)
			14f -> rotationSpeedRadioGroup.check(fastSpeedRadioButton.id)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		// чтобы биндинг и View были собраны сборщиком мусора
		binding = null
	}
}