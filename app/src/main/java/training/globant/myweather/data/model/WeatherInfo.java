package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by francisco on 01/05/18.
 */

public class WeatherInfo {

  @SerializedName("name")
  private String name;
  @SerializedName("weather")
  private List<SkyDescription> skyDescription;
  @SerializedName("main")
  private TemperatureInfo temperatureInfo;

  public WeatherInfo(String name, List<SkyDescription> skyDescription, TemperatureInfo temperatureInfo) {
    this.name = name;
    this.skyDescription = skyDescription;
    this.temperatureInfo = temperatureInfo;
  }

  public WeatherInfo() {
    this.name = "";
    this.skyDescription = null;
    this.temperatureInfo = null;
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

}
