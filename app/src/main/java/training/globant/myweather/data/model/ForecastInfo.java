package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Represents a json that describes the weather from rest service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ForecastInfo {

  private City city;

  @SerializedName("cnt")
  private long forecastAmount;
  @SerializedName("list")
  private List<ForecastItem> list = null;

  /**
   * No args constructor for use in serialization
   */
  public ForecastInfo() {
  }

  /**
   *
   * @param city
   * @param forecastAmount
   * @param list
   */
  public ForecastInfo(City city, long forecastAmount, List<ForecastItem> list) {
    this.city = city;
    this.forecastAmount = forecastAmount;
    this.list = list;
  }

  public City getCity() {
    return city;
  }

  public long getForecastAmount() {
    return forecastAmount;
  }

  public List<ForecastItem> getList() {
    return list;
  }

}
