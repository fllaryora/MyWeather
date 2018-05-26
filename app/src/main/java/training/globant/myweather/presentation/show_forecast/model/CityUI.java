package training.globant.myweather.presentation.show_forecast.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Class that represents the city with forecasts in the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class CityUI implements Parcelable {

  public static final Creator<CityUI> CREATOR = new Creator<CityUI>() {
    @Override
    public CityUI createFromParcel(Parcel in) {
      return new CityUI(in);
    }

    @Override
    public CityUI[] newArray(int size) {
      return new CityUI[size];
    }
  };

  private String cityLabel;
  private List<ForecastItemUI> forecastItemUIList;

  /**
   * @param cityLabel name of city
   * @param forecastItemUIList weather list, predictions
   */
  public CityUI(String cityLabel,
      List<ForecastItemUI> forecastItemUIList) {
    this.cityLabel = cityLabel;
    this.forecastItemUIList = forecastItemUIList;
  }

  /**
   * Construct CityUI
   *
   * @param in CityUI but parceled
   */
  protected CityUI(Parcel in) {
    cityLabel = in.readString();
    forecastItemUIList = in.createTypedArrayList(ForecastItemUI.CREATOR);
  }

  /**
   * Gets name of the city
   *
   * @return name of the city
   */
  public String getCityLabel() {
    return cityLabel;
  }

  /**
   * Gets Forecast ItemUI List
   *
   * @return pedictions
   */
  public List<ForecastItemUI> getForecastItemUIList() {
    return forecastItemUIList;
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
    parcel.writeTypedList(forecastItemUIList);
  }
}
