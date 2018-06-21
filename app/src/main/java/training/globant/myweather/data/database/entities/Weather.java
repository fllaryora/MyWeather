package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.Date;
import training.globant.myweather.data.utils.Constant;

@Entity(tableName = Constant.DB_TABLE_WEATHER)
public class Weather {

  @PrimaryKey(autoGenerate = true)
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
  private double temperature;
  private double maxTemperature;
  private double minTemperature;
  private String skyLabel;
  private int icon;
  //last updated
  private Date lastRefresh;

  public Weather(@NonNull long id, String textSubmited, String latitude, String longitude,
      String cityLabel, String countryLabel, double temperature,
      double maxTemperature, double minTemperature, String skyLabel, int icon,
      Date lastRefresh) {
    this.id = id;
    this.textSubmited = textSubmited;
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityLabel = cityLabel;
    this.countryLabel = countryLabel;
    this.temperature = temperature;
    this.maxTemperature = maxTemperature;
    this.minTemperature = minTemperature;
    this.skyLabel = skyLabel;
    this.icon = icon;
    this.lastRefresh = lastRefresh;
  }

  public Weather() {
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

  public String getSkyLabel() {
    return skyLabel;
  }

  public void setSkyLabel(String skyLabel) {
    this.skyLabel = skyLabel;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
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
