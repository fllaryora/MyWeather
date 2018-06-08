package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import training.globant.myweather.data.utils.Constant;

@Entity(tableName = Constant.DB_TABLE_FORECAST_ITEM)
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

  public ForecastItem(@NonNull long id, int forecastId, String dayLabel, String hourLabel,
      String temperatureLabel, String maxTemperatureLabel, String minTemperatureLabel, int icon) {
    this.id = id;
    this.forecastId = forecastId;
    this.dayLabel = dayLabel;
    this.hourLabel = hourLabel;
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

  public int getForecastId() {
    return forecastId;
  }

  public void setForecastId(int forecastId) {
    this.forecastId = forecastId;
  }

  public String getDayLabel() {
    return dayLabel;
  }

  public void setDayLabel(String dayLabel) {
    this.dayLabel = dayLabel;
  }

  public String getHourLabel() {
    return hourLabel;
  }

  public void setHourLabel(String hourLabel) {
    this.hourLabel = hourLabel;
  }

  public String getTemperatureLabel() {
    return temperatureLabel;
  }

  public void setTemperatureLabel(String temperatureLabel) {
    this.temperatureLabel = temperatureLabel;
  }

  public String getMaxTemperatureLabel() {
    return maxTemperatureLabel;
  }

  public void setMaxTemperatureLabel(String maxTemperatureLabel) {
    this.maxTemperatureLabel = maxTemperatureLabel;
  }

  public String getMinTemperatureLabel() {
    return minTemperatureLabel;
  }

  public void setMinTemperatureLabel(String minTemperatureLabel) {
    this.minTemperatureLabel = minTemperatureLabel;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

}
