package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import training.globant.myweather.data.utils.Constant;

@Entity(tableName = Constant.DB_TABLE_FORECAST_ITEM)
public class ForecastItemDB {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ForeignKey(entity = Forecast.class, parentColumns = "id", childColumns = "forecastId")
  private long forecastId;

  private long dateTime;
  private double temperature;
  private double maxTemperature;
  private double minTemperature;
  private int icon;

  public ForecastItemDB(@NonNull long id, int forecastId, long dateTime,
      double temperature, double maxTemperature, double minTemperature, int icon) {
    this.id = id;
    this.forecastId = forecastId;
    this.dateTime = dateTime;
    this.temperature = temperature;
    this.maxTemperature = maxTemperature;
    this.minTemperature = minTemperature;
    this.icon = icon;
  }

  public ForecastItemDB() {
  }

  @NonNull
  public long getId() {
    return id;
  }

  public void setId(@NonNull long id) {
    this.id = id;
  }

  public long getForecastId() {
    return forecastId;
  }

  public void setForecastId(long forecastId) {
    this.forecastId = forecastId;
  }

  public long getDateTime() {
    return dateTime;
  }

  public void setDateTime(long dateTime) {
    this.dateTime = dateTime;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getMaxTemperature() {
    return maxTemperature;
  }

  public void setMaxTemperature(double maxTemperature) {
    this.maxTemperature = maxTemperature;
  }

  public double getMinTemperature() {
    return minTemperature;
  }

  public void setMinTemperature(double minTemperature) {
    this.minTemperature = minTemperature;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

}
