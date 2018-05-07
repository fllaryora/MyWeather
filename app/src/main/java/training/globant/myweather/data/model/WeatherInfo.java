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
  private List<MetaInfo> metaInfo;
  @SerializedName("main")
  private TemperatureInfo temperatureInfo;

  public WeatherInfo(String name, List<MetaInfo> metaInfo, TemperatureInfo temperatureInfo) {
    this.name = name;
    this.metaInfo = metaInfo;
    this.temperatureInfo = temperatureInfo;
  }

  public WeatherInfo() {
    this.name = "";
    this.metaInfo = null;
    this.temperatureInfo = null;
  }

  public String getName() {
    return name;
  }

  public List<MetaInfo> getMetaInfo() {
    return metaInfo;
  }

  public TemperatureInfo getTemperatureInfo() {
    return temperatureInfo;
  }

}
