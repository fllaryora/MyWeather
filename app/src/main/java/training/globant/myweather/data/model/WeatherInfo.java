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

public class WeatherInfo {

  private String name;
  @SerializedName("weather")
  private List<SkyDescription> skyDescription;
  @SerializedName("main")
  private TemperatureInfo temperatureInfo;
  @SerializedName("sys")
  private CountryHolder country;

  public WeatherInfo(String name, List<SkyDescription> skyDescription,
      TemperatureInfo temperatureInfo, CountryHolder country) {
    this.name = name;
    this.skyDescription = skyDescription;
    this.temperatureInfo = temperatureInfo;
    this.country = country;
  }

  public WeatherInfo() {
    this.name = "";
    this.skyDescription = null;
    this.temperatureInfo = null;
    this.country = null;
  }

  public String getName() {
    return name;
  }

  public List<SkyDescription> getSkyDescription() {
    return skyDescription;
  }

  public TemperatureInfo getTemperatureInfo() {
    return temperatureInfo;
  }

  public CountryHolder getCountry() {
    return country;
  }

}
