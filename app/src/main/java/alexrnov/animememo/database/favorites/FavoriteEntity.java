package alexrnov.animememo.database.favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteEntity {
	@PrimaryKey
	public long id;

	@ColumnInfo(name = "path")
	public String path;

	public FavoriteEntity(long id, String path) {
		this.id = id;
		this.path = path;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) return true;
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;

		FavoriteEntity otherFavorites = (FavoriteEntity) otherObject;

		return (id == otherFavorites.id) && (path.equals(otherFavorites.path));
	}
}
