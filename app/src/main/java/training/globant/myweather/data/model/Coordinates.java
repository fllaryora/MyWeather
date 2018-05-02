package training.globant.myweather.data.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by francisco on 01/05/18.
 */

public class Coordinates {

    //  "lon":-64.18,
    @SerializedName("lon")
    private double longitude;
    //"lat":-31.42
    @SerializedName("lat")
    private double latitude;

    public Coordinates() {
        this.longitude = 0.0d;
        this.latitude = 0.0d;
    }

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
