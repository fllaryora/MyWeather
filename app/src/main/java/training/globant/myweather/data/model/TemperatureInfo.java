package training.globant.myweather.data.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by francisco on 01/05/18.
 */
//temperature in kelvins
//presure in Atmospheric pressure hPa
//humidity in %
public class TemperatureInfo {
    //"temp":288.15,
    @SerializedName("temp")
    private double temperature;
    //"temp_min":288.15,
    @SerializedName("temp_min")
    private double minimalTemperature;
    //"temp_max":288.15
    @SerializedName("temp_max")
    private double maximalTemperature;
    //"pressure":1017,
    @SerializedName("pressure")
    private int pressure;
    //"humidity":100,
    @SerializedName("humidity")
    private int humidity;

    public double getTemperature() {
        return temperature;
    }

    public double getMinimalTemperature() {
        return minimalTemperature;
    }

    public double getMaximalTemperature() {
        return maximalTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public TemperatureInfo(double temperature, double minimalTemperature, double maximalTemperature, int pressure, int humidity) {
        this.temperature = temperature;
        this.minimalTemperature = minimalTemperature;
        this.maximalTemperature = maximalTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public TemperatureInfo() {
        this.temperature = 0.0;
        this.minimalTemperature = 0.0;
        this.maximalTemperature = 0.0;
        this.pressure = 0;
        this.humidity = 0;
    }
}
