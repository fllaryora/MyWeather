package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */

public class ErrorInfo {

  @SerializedName("message")
  private String message;
  @SerializedName("cod")
  private int errorCode;

  public ErrorInfo(String message, int errorCode) {
    this.message = message;
    this.errorCode = errorCode;
  }

  public ErrorInfo() {
    this.message = "";
    this.errorCode = 0;
  }

  public String getMessage() {
    return message;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
