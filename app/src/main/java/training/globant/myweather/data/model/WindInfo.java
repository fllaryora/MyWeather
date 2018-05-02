package training.globant.myweather.data.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by francisco on 01/05/18.
 */

public class WindInfo {
    // Unit Default: meter/sec,
    //"speed":2.6,
    @SerializedName("speed")
    private double speed;
    //https://en.wikipedia.org/wiki/Points_of_the_compass
    //Wind direction, degrees (meteorological)
    //"deg":0 azimuth > N
    //"deg":90 azimuth > E
    //"deg":180 azimuth > S
    //"deg":270 azimuth > W
    @SerializedName("deg")
    private double direction;

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public WindInfo(double speed, double direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public WindInfo() {
        this.speed = 0.0;
        this.direction = 0.0;
    }

}
