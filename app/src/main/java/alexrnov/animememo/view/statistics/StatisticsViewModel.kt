package alexrnov.animememo.view.statistics

import alexrnov.animememo.Initialization.STATISTICS_DB
import alexrnov.animememo.database.statistics.StatisticsDatabase
import alexrnov.animememo.database.statistics.StatisticsEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
	private val sortByDate = MutableLiveData<Boolean>().apply { false }
	private val sortByCards = MutableLiveData<Boolean>().apply { false }
	private val sortByErrors = MutableLiveData<Boolean>().apply { false }

	private val _texts = MutableLiveData<List<StatisticsEntity>>().apply {
		val db: StatisticsDatabase = Room.databaseBuilder(
			application.applicationContext,
			StatisticsDatabase::class.java, STATISTICS_DB
		).allowMainThreadQueries().build()

		val requests = db.requests()
		value = requests.all

		/*
		// тест большого количества записей
		val testList = mutableListOf<StatisticsEntity>()
		(0..15).forEachIndexed { index, it ->
			testList.add(StatisticsEntity(index.toLong(), "data", 1, index))
		}
		value = testList
		*/
	}

	var texts: LiveData<List<StatisticsEntity>> = _texts

	fun clearData() {
		_texts.value = null
	}

	fun sortByDate() {
		sortByCards.value = false
		sortByErrors.value = false

		if (sortByDate.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedByDescending {
				val formatter = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.getDefault())
				val date: Date? = formatter.parse(it.date)
				date
			}
			sortByDate.value = true
		}
	}

	fun sortByCards() {
		sortByDate.value = false
		sortByErrors.value = false

		if (sortByCards.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedBy { it.cardsQuantity }
			sortByCards.value = true
		}
	}

	fun sortByErrors() {
		sortByDate.value = false
		sortByCards.value = false

		if (sortByErrors.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedBy { it.errors }
			sortByErrors.value = true
		}
	}
}