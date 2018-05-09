package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */
public class TemperatureInfo {

  @SerializedName("temp")
  private double temperature;
  @SerializedName("temp_min")
  private double minimalTemperature;
  @SerializedName("temp_max")
  private double maximalTemperature;

  public TemperatureInfo(double temperature, double minimalTemperature, double maximalTemperature) {
    this.temperature = temperature;
    this.minimalTemperature = minimalTemperature;
    this.maximalTemperature = maximalTemperature;
  }

  public TemperatureInfo() {
    this.temperature = 0.0;
    this.minimalTemperature = 0.0;
    this.maximalTemperature = 0.0;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getMinimalTemperature() {
    return minimalTemperature;
  }

  public double getMaximalTemperature() {
    return maximalTemperature;
  }

}
