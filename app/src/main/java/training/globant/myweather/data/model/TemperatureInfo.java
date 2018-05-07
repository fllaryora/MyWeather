package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */
public class TemperatureInfo {

  @SerializedName("temp")
  private double temperature;

  public TemperatureInfo(double temperature) {
    this.temperature = temperature;
  }

  public TemperatureInfo() {
    this.temperature = 0.0;
  }

  public double getTemperature() {
    return temperature;
  }
}
