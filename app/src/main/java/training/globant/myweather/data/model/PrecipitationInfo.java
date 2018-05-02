package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */

public class PrecipitationInfo {
    //Rain/Snow volume for the last 3 hours
    @SerializedName("3h")
    private String value;
}
