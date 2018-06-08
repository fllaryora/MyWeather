package training.globant.myweather.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.Date;
import training.globant.myweather.data.database.entities.Weather;

@Dao
public interface WeatherDAO {

  @Query("SELECT weather_table.* FROM weather_table "
      + " WHERE weather_table.textSubmited = :text "
      + " AND weather_table.lastRefresh > :lastRefreshMax LIMIT 1")
  public Weather getWeather(String text,Date lastRefreshMax);

  @Query("SELECT weather_table.* FROM weather_table "
      + " WHERE weather_table.latitude = :lat "
      + " AND weather_table.longitude = :lon "
      + " AND weather_table.lastRefresh > :lastRefreshMax LIMIT 1")
  public Weather getWeather(String lat,String lon,Date lastRefreshMax);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Weather weather);

}
