package alexrnov.animememo.database.favorites;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// exportSchema = false, чтобы не возникала ошибка при сборке apk
@Database(entities = {FavoriteEntity.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {
	public abstract FavoritesRequests requests();
}
