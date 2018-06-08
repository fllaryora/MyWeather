package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.Date;
import java.util.List;
import training.globant.myweather.data.utils.Constant;


@Entity(tableName = Constant.DB_TABLE_FORECAST)
public class Forecast {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ColumnInfo(name = "textSubmited")
  private String textSubmited;

  @ColumnInfo(name = "latitude")
  private String latitude;

  @ColumnInfo(name = "longitude")
  private String longitude;

  private String cityLabel;
  private String countryLabel;

  @Ignore
  private List<ForecastItem> forecastItems;

  //last updated
  private Date lastRefresh;

  public Forecast(@NonNull long id, String textSubmited, String latitude, String longitude,
      String cityLabel, String countryLabel,
      List<ForecastItem> forecastItems, Date lastRefresh) {
    this.id = id;
    this.textSubmited = textSubmited;
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityLabel = cityLabel;
    this.countryLabel = countryLabel;
    this.forecastItems = forecastItems;
    this.lastRefresh = lastRefresh;
  }

  public Forecast() {
  }

  @NonNull
  public long getId() {
    return id;
  }

  public void setId(@NonNull long id) {
    this.id = id;
  }

  public String getCityLabel() {
    return cityLabel;
  }

  public void setCityLabel(String cityLabel) {
    this.cityLabel = cityLabel;
  }

  public String getCountryLabel() {
    return countryLabel;
  }

  public void setCountryLabel(String countryLabel) {
    this.countryLabel = countryLabel;
  }

  public List<ForecastItem> getForecastItems() {
    return forecastItems;
  }

  public void setForecastItems(
      List<ForecastItem> forecastItems) {
    this.forecastItems = forecastItems;
  }

  public Date getLastRefresh() {
    return lastRefresh;
  }

  public void setLastRefresh(Date lastRefresh) {
    this.lastRefresh = lastRefresh;
  }

  public String getTextSubmited() {
    return textSubmited;
  }

  public void setTextSubmited(String textSubmited) {
    this.textSubmited = textSubmited;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }
}
