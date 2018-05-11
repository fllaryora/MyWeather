package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a json that describes the temperature from rest service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class TemperatureInfo {

  @SerializedName("temp")
  private double temperature;
  @SerializedName("temp_min")
  private double minimum;
  @SerializedName("temp_max")
  private double maximum;

  public TemperatureInfo(double temperature, double minimum, double maximum) {
    this.temperature = temperature;
    this.minimum = minimum;
    this.maximum = maximum;
  }

  public TemperatureInfo() {
    this.temperature = 0.0;
    this.minimum = 0.0;
    this.maximum = 0.0;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getMinimum() {
    return minimum;
  }

  public double getMaximum() {
    return maximum;
  }

}
