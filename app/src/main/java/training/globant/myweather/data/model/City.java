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

  @SerializedName("name")
  private String cityName;

  /**
   * No args constructor for use in serialization
   */
  public City() {
  }

  /**
   * @param cityName
   */
  public City(String cityName) {
    this.cityName = cityName;
  }

  public String getCityName() {
    return cityName;
  }
}
