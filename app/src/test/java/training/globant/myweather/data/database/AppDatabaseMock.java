package training.globant.myweather.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import training.globant.myweather.data.database.converters.DateConverter;
import training.globant.myweather.data.database.dao.ForecastDAO;
import training.globant.myweather.data.database.dao.WeatherDAO;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItemDB;
import training.globant.myweather.data.database.entities.Weather;

/**
 * In-Memory Room Database definition
 */
@Database(entities = {Forecast.class, ForecastItemDB.class, Weather.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabaseMock  extends RoomDatabase{

  private static AppDatabaseMock INSTANCE;

  public abstract ForecastDAO getForecastDAO();
  public abstract WeatherDAO getWeatherDAO();

  /**
   * Create a instance of database using room
   * @param context of activity
   * @return INSTANCE, which contain the implementation of AppDatabase
   */
  public static AppDatabaseMock getAppDatabase(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
          AppDatabaseMock.class)
          .allowMainThreadQueries()
          .build();
    }
    return INSTANCE;
  }
}
