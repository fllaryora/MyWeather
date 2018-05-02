package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */

public class ErrorInfo {
    //"message": "",
    @SerializedName("message")
    private String message;
    // "cod": "404"
    @SerializedName("cod")
    private int errorCode;
    //400 nothing to geocode
    //401 invalid api key
    //403 the data is available only for paid accounts

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ErrorInfo(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ErrorInfo() {
        this.message = "";
        this.errorCode = 0;
    }
}
