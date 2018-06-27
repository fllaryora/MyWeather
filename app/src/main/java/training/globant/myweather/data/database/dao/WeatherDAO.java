package training.globant.myweather.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;
import training.globant.myweather.data.database.entities.Weather;

@Dao
public interface WeatherDAO {

  @Query("SELECT weather_table.* FROM weather_table")
  List<Weather> getWeathers();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Weather weather);

  @Delete
  void delete(Weather weather);

}
