package alexrnov.animememo.db

import alexrnov.animememo.database.favorites.FavoriteEntity
import alexrnov.animememo.database.favorites.FavoritesDatabase
import alexrnov.animememo.database.favorites.FavoritesRequests
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoritesTest {
	private lateinit var favoritesDatabase: FavoritesDatabase
	private lateinit var favoritesRequests: FavoritesRequests

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		favoritesDatabase = Room.inMemoryDatabaseBuilder(context, FavoritesDatabase::class.java).build()
		favoritesRequests = favoritesDatabase.requests()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		favoritesDatabase.close()
	}

	@Test
	@Throws(Exception::class)
	fun writeAndReadStatisticsEntity() {
		val path = "/test_path/file.jpg"
		val favoriteEntity = FavoriteEntity(favoritesRequests.lastCardId + 1, path)
		favoritesRequests.insert(favoriteEntity)

		assertThat(favoritesRequests.all.size).isEqualTo(1)
		assertThat(favoritesRequests.getCountFavorites()).isEqualTo(1)

		assertThat(favoritesRequests.all[0].id).isEqualTo(favoritesRequests.lastCardId)
		assertThat(favoritesRequests.all[0].path).isEqualTo(path)
	}

	@Test
	@Throws(Exception::class)
	fun addAndDeleteStatisticsEntity() {
		val path = "/test_path/file.jpg"
		val otherPath = "/test_path/otherFile.jpg"
		val favoriteEntity = FavoriteEntity(favoritesRequests.lastCardId + 1, path)
		favoritesRequests.insert(favoriteEntity)

		assertThat(favoritesRequests.isPathExists(path)).isTrue()
		assertThat(favoritesRequests.isPathExists(otherPath)).isFalse()

		favoritesRequests.deleteByPath(path)
		assertThat(favoritesRequests.isPathExists(path)).isFalse()
		assertThat(favoritesRequests.isPathExists(otherPath)).isFalse()
	}
}