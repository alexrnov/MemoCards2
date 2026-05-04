package alexrnov.animememo.database.statistics;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// exportSchema = false, чтобы не возникала ошибка при сборке apk
@Database(entities = {StatisticsEntity.class}, version = 1, exportSchema = false)
public abstract class StatisticsDatabase extends RoomDatabase {
	public abstract StatisticsRequests requests();
}
