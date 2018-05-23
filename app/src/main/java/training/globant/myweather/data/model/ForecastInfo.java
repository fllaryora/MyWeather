package training.globant.myweather.data.model;

/**
 * Represents a json that describes the weather from rest service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ForecastInfo {

  private City city;

  /**
   * No args constructor for use in serialization
   */
  public ForecastInfo() {
  }

  /**
   *
   * @param city
   */
  public ForecastInfo(City city) {
    this.city = city;
  }

  public City getCity() {
    return city;
  }

}
