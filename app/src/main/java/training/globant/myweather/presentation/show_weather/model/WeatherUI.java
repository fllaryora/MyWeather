package training.globant.myweather.presentation.show_weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UI model of a Weather.
 * Parcelable data to transfer between fragments and to save/restore on orientation changes.
 * Created by francisco on 29/04/18.
 */

public class WeatherUI implements Parcelable {

  public static final Creator<WeatherUI> CREATOR = new Creator<WeatherUI>() {
    @Override
    public WeatherUI createFromParcel(Parcel in) {
      return new WeatherUI(in);
    }

    @Override
    public WeatherUI[] newArray(int size) {
      return new WeatherUI[size];
    }
  };

  private String cityLabel;
  private String temperatureLabel;
  private String maxTemperatureLabel;
  private String minTemperatureLabel;
  private String skyLabel;
  private int icon;

  public WeatherUI(String cityLabel, String maxTemperatureLabel, String minTemperatureLabel,
      String temperatureLabel, String skyLabel, int icon) {
    this.cityLabel = cityLabel;
    this.maxTemperatureLabel = maxTemperatureLabel;
    this.minTemperatureLabel = minTemperatureLabel;
    this.temperatureLabel = temperatureLabel;
    this.skyLabel = skyLabel;
    this.icon = icon;
  }

  protected WeatherUI(Parcel in) {
    cityLabel = in.readString();
    maxTemperatureLabel = in.readString();
    minTemperatureLabel = in.readString();
    temperatureLabel = in.readString();
    skyLabel = in.readString();
    icon = in.readInt();
  }

  public String getCityLabel() {
    return cityLabel;
  }

  public String getTemperatureLabel() {
    return temperatureLabel;
  }

  public String getSkyLabel() {
    return skyLabel;
  }

  public String getMaxTemperatureLabel() {
    return maxTemperatureLabel;
  }

  public String getMinTemperatureLabel() {
    return minTemperatureLabel;
  }

  public int getIcon() {
    return icon;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(cityLabel);
    parcel.writeString(maxTemperatureLabel);
    parcel.writeString(minTemperatureLabel);
    parcel.writeString(temperatureLabel);
    parcel.writeString(skyLabel);
    parcel.writeInt(icon);
  }
}
