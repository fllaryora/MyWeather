package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import training.globant.myweather.data.utils.Constant;

@Entity(tableName = Constant.DB_TABLE_FORECAST_ITEM)
public class ForecastItem {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ForeignKey(entity = Forecast.class, parentColumns = "id", childColumns = "forecastId")
  private long forecastId;

  private long dateTime;
  private double temperatureLabel;
  private double maxTemperatureLabel;
  private double minTemperatureLabel;
  private int icon;

  public ForecastItem(@NonNull long id, int forecastId, long dateTime,
      double temperatureLabel, double maxTemperatureLabel, double minTemperatureLabel, int icon) {
    this.id = id;
    this.forecastId = forecastId;
    this.dateTime = dateTime;
    this.temperatureLabel = temperatureLabel;
    this.maxTemperatureLabel = maxTemperatureLabel;
    this.minTemperatureLabel = minTemperatureLabel;
    this.icon = icon;
  }

  public ForecastItem() {
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

  public double getTemperatureLabel() {
    return temperatureLabel;
  }

  public void setTemperatureLabel(double temperatureLabel) {
    this.temperatureLabel = temperatureLabel;
  }

  public double getMaxTemperatureLabel() {
    return maxTemperatureLabel;
  }

  public void setMaxTemperatureLabel(double maxTemperatureLabel) {
    this.maxTemperatureLabel = maxTemperatureLabel;
  }

  public double getMinTemperatureLabel() {
    return minTemperatureLabel;
  }

  public void setMinTemperatureLabel(double minTemperatureLabel) {
    this.minTemperatureLabel = minTemperatureLabel;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

}
