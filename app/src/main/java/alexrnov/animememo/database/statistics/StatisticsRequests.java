package alexrnov.animememo.database.statistics;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface StatisticsRequests {
	@Query("SELECT COUNT(*) FROM StatisticsEntity")
	int getCountGames();

	@Query("SELECT * FROM StatisticsEntity")
	List<StatisticsEntity> getAll();

	@Query("SELECT id FROM StatisticsEntity ORDER BY id DESC LIMIT 1")
	long getLastGameId();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(StatisticsEntity statisticsEntity);

	@Query("DELETE FROM StatisticsEntity")
	void deleteAllEntities();

	@Transaction
	default void insertWithLimit(StatisticsEntity statisticsEntity) {
		insert(statisticsEntity);
		deleteOldest(); // оставляет только последние n-записей
	}

	// вставить в таблицу новую запись и если превышено количество записей, то удалить самую старую
	@Query("DELETE FROM StatisticsEntity WHERE id NOT IN (SELECT id FROM StatisticsEntity ORDER BY id DESC LIMIT 1000)")
	void deleteOldest();
}
