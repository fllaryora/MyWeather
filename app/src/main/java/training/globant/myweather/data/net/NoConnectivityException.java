package training.globant.myweather.data.net;

import java.io.IOException;

public class NoConnectivityException extends IOException {
  @Override
  public String getMessage() {
    return "Network Connection exception";
  }

}
