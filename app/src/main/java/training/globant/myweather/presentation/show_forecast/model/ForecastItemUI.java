package training.globant.myweather.presentation.show_forecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents the Forecast Item in the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ForecastItemUI implements Parcelable {

  public static final Creator<ForecastItemUI> CREATOR = new Creator<ForecastItemUI>() {
    @Override
    public ForecastItemUI createFromParcel(Parcel in) {
      return new ForecastItemUI(in);
    }

    @Override
    public ForecastItemUI[] newArray(int size) {
      return new ForecastItemUI[size];
    }
  };

  private String dayLabel;
  private String hourLabel;
  private String temperatureLabel;
  private String maxTemperatureLabel;
  private String minTemperatureLabel;
  private int icon;

  /**
   * @param dayLabel monday, thuesday, and so on
   * @param hourLabel 08:00hs
   * @param temperatureLabel current temp
   * @param maxTemperatureLabel max temp
   * @param minTemperatureLabel min temp
   * @param icon R resource drawable icon
   */

  public ForecastItemUI(String dayLabel, String hourLabel, String temperatureLabel,
      String maxTemperatureLabel, String minTemperatureLabel, int icon) {
    this.dayLabel = dayLabel;
    this.hourLabel = hourLabel;
    this.temperatureLabel = temperatureLabel;
    this.maxTemperatureLabel = maxTemperatureLabel;
    this.minTemperatureLabel = minTemperatureLabel;
    this.icon = icon;
  }

  /**
   * Construct ForecastItemUI
   *
   * @param in ForecastItemUI but parceled
   */
  protected ForecastItemUI(Parcel in) {
    dayLabel = in.readString();
    hourLabel = in.readString();
    temperatureLabel = in.readString();
    maxTemperatureLabel = in.readString();
    minTemperatureLabel = in.readString();
    icon = in.readInt();
  }

  /**
   * Gets the week day
   *
   * @return week day
   */
  public String getDayLabel() {
    return dayLabel;
  }

  /**
   * Gets hours in 24 format
   */
  public String getHourLabel() {
    return hourLabel;
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
    parcel.writeString(dayLabel);
    parcel.writeString(hourLabel);
    parcel.writeString(temperatureLabel);
    parcel.writeString(maxTemperatureLabel);
    parcel.writeString(minTemperatureLabel);
    parcel.writeInt(icon);
  }
}
