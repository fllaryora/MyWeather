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
public class City {


  @SerializedName("cnt")
  private long forecastAmount;
  @SerializedName("list")
  private java.util.List<ForecastItem> list = null;

  /**
   * No args constructor for use in serialization
   */
  public City() {
  }

  /**
   * @param forecastAmount
   * @param list
   */
  public City(long forecastAmount, List<ForecastItem> list) {

    this.forecastAmount = forecastAmount;
    this.list = list;
  }

  public long getForecastAmount() {
    return forecastAmount;
  }

  public List<ForecastItem> getList() {
    return list;
  }

}
