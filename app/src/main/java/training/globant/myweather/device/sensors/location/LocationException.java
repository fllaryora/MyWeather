package training.globant.myweather.device.sensors.location;

import training.globant.myweather.device.utils.DeviceConstant;

/**
 * Hold the cause why we can't make a call for location
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class LocationException extends Exception {

  public LocationException(boolean hasSystemFeature, boolean isProviderEnabled, boolean isSecure) {
    super(String.format(DeviceConstant.EXCEPTION_FORMAT, hasSystemFeature,
        isProviderEnabled, isSecure));
  }

  public LocationException(SecurityException se) {
    super(se.getMessage());
  }
}
