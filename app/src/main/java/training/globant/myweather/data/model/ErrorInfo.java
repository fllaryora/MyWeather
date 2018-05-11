package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a json error message from rest service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ErrorInfo {

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
