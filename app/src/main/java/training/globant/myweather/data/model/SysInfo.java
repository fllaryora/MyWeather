package training.globant.myweather.data.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by francisco on 01/05/18.
 */

public class SysInfo {
    //"id":4688, internal parameter
    @SerializedName("id")
    private int id;
    //"message":0.004, internal parameter
    @SerializedName("message")
    private double message;
    //"country":"AR",
    @SerializedName("country")
    private String country;
    //"type":1, internal parameter
    @SerializedName("type")
    private int type;
    //"sunrise":1525171700, time, unix, UTC
    @SerializedName("sunrise")
    private long sunrise;
    // "sunset":1525210723  time, unix, UTC
    @SerializedName("sunset")
    private long sunset;

}
