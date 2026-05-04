package alexrnov.animememo.database.favorites;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesRequests {
	@Query("SELECT COUNT(*) FROM FavoriteEntity")
	int getCountFavorites();

	@Query("SELECT * FROM FavoriteEntity")
	List<FavoriteEntity> getAll();

	@Query("SELECT id FROM FavoriteEntity ORDER BY id DESC LIMIT 1")
	long getLastCardId();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(FavoriteEntity favoriteEntity);

	@Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE path = :path LIMIT 1)")
	boolean isPathExists(String path);

	@Query("DELETE FROM FavoriteEntity WHERE path = :path")
	void deleteByPath(String path);
}
