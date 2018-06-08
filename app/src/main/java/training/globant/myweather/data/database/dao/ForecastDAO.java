package training.globant.myweather.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.Date;
import java.util.List;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItem;

@Dao
public interface ForecastDAO {

  @Query("SELECT forecast_table.* FROM forecast_table "
      + " WHERE forecast_table.textSubmited = :text "
      + " AND forecast_table.lastRefresh > :lastRefreshMax LIMIT 1")
  public Forecast getForecast(String text,Date lastRefreshMax);

  @Query("SELECT forecast_table.* FROM forecast_table "
      + " WHERE forecast_table.latitude = :lat "
      + " AND forecast_table.longitude = :lon "
      + " AND forecast_table.lastRefresh > :lastRefreshMax LIMIT 1")
  public Forecast getForecast(String lat,String lon,Date lastRefreshMax);

  @Query("SELECT forecast_item_table.* FROM forecast_item_table "
      + "WHERE forecast_item_table.forecastId = :forecastId")
  List<ForecastItem> getForecastItems(int forecastId);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertForecast(Forecast forecast);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertForecastItem(ForecastItem forecastItem);

}
