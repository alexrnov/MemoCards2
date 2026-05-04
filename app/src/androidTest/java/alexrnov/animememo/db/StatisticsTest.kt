package alexrnov.animememo.db

import alexrnov.animememo.database.statistics.StatisticsDatabase
import alexrnov.animememo.database.statistics.StatisticsEntity
import alexrnov.animememo.database.statistics.StatisticsRequests
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
class StatisticsTest {
	private lateinit var statisticsDatabase: StatisticsDatabase
	private lateinit var statisticsRequests: StatisticsRequests

	private val date = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.getDefault()).format(Date())

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		statisticsDatabase = Room.inMemoryDatabaseBuilder(context, StatisticsDatabase::class.java).build()
		statisticsRequests = statisticsDatabase.requests()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		statisticsDatabase.close()
	}

	@Test
	@Throws(Exception::class)
	fun writeAndReadStatisticsEntity() {
		val cards = 12
		val errors = 2

		val statisticsEntity = createStatisticsEntity(cards, errors)
		statisticsRequests.insert(statisticsEntity)

		val game = statisticsRequests.all[0]
		assertThat(date).isEqualTo(game.date)
		assertThat(cards).isEqualTo(game.cardsQuantity)
		assertThat(errors).isEqualTo(game.errors)
	}

	@Test
	@Throws(Exception::class)
	fun addAndRemoveEntities() {
		assertThat(statisticsRequests.all.size).isEqualTo(0)
		assertThat(statisticsRequests.countGames).isEqualTo(0)
		val statisticsEntity = createStatisticsEntity(12, 2)
		statisticsRequests.insert(statisticsEntity)
		val statisticsEntity2 = createStatisticsEntity(12, 4)
		statisticsRequests.insert(statisticsEntity2)
		assertThat(statisticsRequests.all.size).isEqualTo(2)
		assertThat(statisticsRequests.countGames).isEqualTo(2)
		statisticsRequests.deleteAllEntities()
		assertThat(statisticsRequests.all.size).isEqualTo(0)
		assertThat(statisticsRequests.countGames).isEqualTo(0)
	}

	@Test
	@Throws(Exception::class)
	fun insertWithLimits() {
		assertThat(statisticsRequests.all.size).isEqualTo(0)
		assertThat(statisticsRequests.countGames).isEqualTo(0)
		repeat(1004) {
			val statisticsEntity = createStatisticsEntity(12, 2)
			statisticsRequests.insertWithLimit(statisticsEntity)
		}
		assertThat(statisticsRequests.all.size).isEqualTo(1000)
		assertThat(statisticsRequests.countGames).isEqualTo(1000)

		// первые самые старые 4 записи удалились
		assertThat(statisticsRequests.all[0].id).isEqualTo(5)
		assertThat(statisticsRequests.all[1].id).isEqualTo(6)
		assertThat(statisticsRequests.all[2].id).isEqualTo(7)
		assertThat(statisticsRequests.all[3].id).isEqualTo(8)

		assertThat(statisticsRequests.all[statisticsRequests.all.size - 1].id).isEqualTo(1004)
	}

	fun createStatisticsEntity(cards: Int, errors: Int): StatisticsEntity {
		val gameId = statisticsRequests.lastGameId + 1
		val statisticsEntity = StatisticsEntity(gameId, date, cards, errors)
		return statisticsEntity
	}
}
