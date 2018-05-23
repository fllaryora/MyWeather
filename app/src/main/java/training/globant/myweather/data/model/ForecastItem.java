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

public class ForecastItem {

  @SerializedName("main")
  private TemperatureInfo temperatureInfo;
  @SerializedName("weather")
  private List<SkyDescription> skyDescriptions = null;


  /**
   * No args constructor for use in serialization
   */
  public ForecastItem() {
  }

  /**
   * @param skyDescriptions
   * @param temperatureInfo
   */
  public ForecastItem(TemperatureInfo temperatureInfo, List<SkyDescription> skyDescriptions) {
    this.temperatureInfo = temperatureInfo;
    this.skyDescriptions = skyDescriptions;

  }

  public TemperatureInfo getTemperatureInfo() {
    return temperatureInfo;
  }

  public List<SkyDescription> getWeather() {
    return skyDescriptions;
  }

}
