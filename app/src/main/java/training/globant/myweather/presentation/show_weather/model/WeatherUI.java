package training.globant.myweather.presentation.show_weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents the weather in the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
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
  private String countryLabel;
  private String temperatureLabel;
  private String maxTemperatureLabel;
  private String minTemperatureLabel;
  private String skyLabel;
  private int icon;

  /**
   * Construct WeatherUI
   *
   * @param cityLabel name of the city
   * @param maxTemperatureLabel max temperature label
   * @param minTemperatureLabel min temperature label
   * @param temperatureLabel current temperature label
   * @param skyLabel description of the situation in the sky
   * @param icon R resource drawable icon
   * @param countryLabel name of the country
   */
  public WeatherUI(String cityLabel, String maxTemperatureLabel, String minTemperatureLabel,
      String temperatureLabel, String skyLabel, int icon, String countryLabel) {
    this.cityLabel = cityLabel;
    this.countryLabel = countryLabel;
    this.maxTemperatureLabel = maxTemperatureLabel;
    this.minTemperatureLabel = minTemperatureLabel;
    this.temperatureLabel = temperatureLabel;
    this.skyLabel = skyLabel;
    this.icon = icon;
  }

  /**
   * Construct WeatherUI
   *
   * @param in WeatherUI but parceled
   */
  protected WeatherUI(Parcel in) {
    cityLabel = in.readString();
    countryLabel = in.readString();
    maxTemperatureLabel = in.readString();
    minTemperatureLabel = in.readString();
    temperatureLabel = in.readString();
    skyLabel = in.readString();
    icon = in.readInt();
  }

  /**
   * Gets the name of the city
   *
   * @return cityLabel
   */
  public String getCityLabel() {
    return cityLabel;
  }

  /**
   * Gets the name of the country
   *
   * @return countryLabel
   */
  public String getCountryLabel() {
    return countryLabel;
  }

  /**
   * Gets the current temperature of the city
   *
   * @return the current temperature of the city
   */
  public String getTemperatureLabel() {
    return temperatureLabel;
  }

  /**
   * Gets a description of the sky
   *
   * @return a description of the sky
   */
  public String getSkyLabel() {
    return skyLabel;
  }

  /**
   * Gets the max temperature of the city in the current day
   *
   * @return the max temperature of the city in the current day
   */
  public String getMaxTemperatureLabel() {
    return maxTemperatureLabel;
  }

  /**
   * Gets the min temperature of the city in the current day
   *
   * @return the min temperature of the city in the current day
   */
  public String getMinTemperatureLabel() {
    return minTemperatureLabel;
  }

  /**
   * Gets R resource drawable of the current status in the sky of the city
   *
   * @return R resource drawable of the current status in the sky of the city
   */
  public int getIcon() {
    return icon;
  }

  /**
   * Describe the kinds of special objects contained in this Parcelable instance's marshaled
   * representation.
   *
   * @return a bitmask indicating the set of special object types marshaled by this Parcelable
   * object instance.
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Flatten this object in to a Parcel.
   *
   * @param parcel The Parcel in which the object should be written.
   * @param i Additional flags about how the object should be written.
   */
  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(cityLabel);
    parcel.writeString(countryLabel);
    parcel.writeString(maxTemperatureLabel);
    parcel.writeString(minTemperatureLabel);
    parcel.writeString(temperatureLabel);
    parcel.writeString(skyLabel);
    parcel.writeInt(icon);
  }
}
