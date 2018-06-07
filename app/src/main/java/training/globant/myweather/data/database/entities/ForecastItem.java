package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "forecast_item_table")
public class ForecastItem {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ForeignKey(entity = Forecast.class, parentColumns = "id", childColumns = "forecastId")
  private int forecastId;

  private String dayLabel;
  private String hourLabel;
  private String temperatureLabel;
  private String maxTemperatureLabel;
  private String minTemperatureLabel;
  private int icon;


}
