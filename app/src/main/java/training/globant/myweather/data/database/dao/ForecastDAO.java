package training.globant.myweather.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItemDB;

@Dao
public interface ForecastDAO {

  @Query("SELECT forecast_table.* FROM forecast_table ")
  List<Forecast> getForecast();

  @Query("SELECT forecast_item_table.* FROM forecast_item_table "
      + "WHERE forecast_item_table.forecastId = :forecastId")
  List<ForecastItemDB> getForecastItems(long forecastId);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(Forecast forecast);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertItem(ForecastItemDB forecastItem);

  @Delete
  void deleteForecast(Forecast forecast);

  @Delete
  void deleteForecastItem(ForecastItemDB forecastItem);
}
