package alexrnov.animememo.view.statistics

import alexrnov.animememo.Initialization.STATISTICS_DB
import alexrnov.animememo.R
import alexrnov.animememo.database.statistics.StatisticsDatabase
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar

class StatisticsActivity : AppCompatActivity() {
	private lateinit var confirmClearDialog: ConstraintLayout
	private lateinit var clearButton: Button
	private lateinit var emptyTextView: TextView
	private lateinit var headContainer: ConstraintLayout

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_statistics)

		val toolbar = findViewById<MaterialToolbar>(R.id.statisticsAppBar)
		setSupportActionBar(toolbar)

		supportActionBar?.let {
			it.setDisplayHomeAsUpEnabled(true)
			it.setDisplayShowHomeEnabled(true)
		}

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statisticsContainer)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val db: StatisticsDatabase = Room.databaseBuilder(
			application.applicationContext,
			StatisticsDatabase::class.java, STATISTICS_DB
		).allowMainThreadQueries().build()

		val countGames = db.requests().countGames

		confirmClearDialog = findViewById(R.id.clearDialogBackground)
		headContainer = findViewById(R.id.headContainer)
		clearButton = findViewById(R.id.clearStatisticsButton)
		emptyTextView = findViewById(R.id.emptyTextView)

		if (countGames < 1) {
			clearButton.isEnabled = false
			clearButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.disable_button))
			emptyTextView.visibility = View.VISIBLE
			headContainer.visibility = View.GONE
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putBoolean("clearDialogVisibility", confirmClearDialog.visibility == View.VISIBLE)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		val isVisible = savedInstanceState.getBoolean("clearDialogVisibility")
		confirmClearDialog.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	fun invokeConfirmDialog(view: View) {
		confirmClearDialog.visibility = View.VISIBLE
	}

	fun backToStatistics(view: View) {
		confirmClearDialog.visibility = View.GONE
	}

	fun clearStatistics(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.clearData()

		clearButton.isEnabled = false
		clearButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.disable_button))

		headContainer.visibility = View.GONE
		confirmClearDialog.visibility = View.GONE
		emptyTextView.visibility = View.VISIBLE

		val db: StatisticsDatabase = Room.databaseBuilder(
			application.applicationContext,
			StatisticsDatabase::class.java, STATISTICS_DB
		).allowMainThreadQueries().build()

		val requests = db.requests()
		requests.deleteAllEntities()
	}

	fun sortByDate(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByDate()
	}

	fun sortByCards(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByCards()
	}

	fun sortByErrors(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByErrors()
	}
}