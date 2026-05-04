package alexrnov.animememo;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import alexrnov.animememo.database.favorites.FavoritesDatabase;
import alexrnov.animememo.database.favorites.FavoritesRequests;
import alexrnov.animememo.database.statistics.StatisticsDatabase;
import alexrnov.animememo.database.statistics.StatisticsRequests;

public class Initialization extends Application {
	public static SharedPreferences appStorage;

	public static final String STATISTICS_DB = "statistics_db";
	public static final String FAVORITES_DB = "favorites_db";

	@Override
	public void onCreate() {
		super.onCreate();

		final String packageName = this.getApplicationContext().getPackageName();
		appStorage = this.getSharedPreferences(packageName, MODE_PRIVATE);

		AsyncTask.execute(() -> {
			StatisticsDatabase db = Room.databaseBuilder(this.getApplicationContext(), StatisticsDatabase.class, STATISTICS_DB).addCallback(dbCallbackStatistics).build();
			StatisticsRequests dao = db.requests();
			int size = dao.getAll().size(); // фактически база будет создана при этой инструкции
		});

		AsyncTask.execute(() -> {
			FavoritesDatabase db = Room.databaseBuilder(this.getApplicationContext(), FavoritesDatabase.class, FAVORITES_DB).addCallback(dbCallbackFavorites).build();
			FavoritesRequests dao = db.requests();
			int size = dao.getAll().size(); // фактически база будет создана при этой инструкции
		});
	}

	RoomDatabase.Callback dbCallbackStatistics = new RoomDatabase.Callback() {
		/** метод вызывается при создании базы данных */
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
		// Log.i("memo", "CREATE DATABASE STATISTICS");
		}
	};

	RoomDatabase.Callback dbCallbackFavorites = new RoomDatabase.Callback() {
		/** метод вызывается при создании базы данных */
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
		// Log.i("memo", "CREATE DATABASE FAVORITES");
		}
	};
}
