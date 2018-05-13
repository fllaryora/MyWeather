package training.globant.myweather.device.utils;

import android.Manifest;
import java.util.Arrays;
import java.util.List;

/**
 * Holds the constants of the project regardless main configurations
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class DeviceConstant {

  public static String FAIL_MESSAGE = "GeoLocationProvider fail, cause: %s";
  public static String EXCEPTION_FORMAT = "hasSystemFeature: %s isProviderEnabled: %sisSecure: %s";
  public enum RequestCodes {
    REQUEST_GPS(1),
    NOT_AVAILABLE(0);
    int requestCode;
    RequestCodes(int requestCode) {
      this.requestCode = requestCode;
    }
    public static RequestCodes fromInt(int code) {

      for (RequestCodes w : RequestCodes.values()) {
        if (w.requestCode == code) {
          return w;
        }
      }
      return NOT_AVAILABLE;
    }
    public int getRequestCode() {
      return this.requestCode;
    }
  }
  public static final String[] accessLocations = new String[]{
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

  private static final List accessLocationsList = Arrays.asList(accessLocations);

  public static boolean isAccessInAccessLocation(String access){
    return (accessLocationsList.contains(access));
  }

  // The minimum distance to change updates in meters
  public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

  // The minimum time between updates in milliseconds
  public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
}
