package training.globant.myweather.device.sensors.location;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import training.globant.myweather.device.utils.DeviceConstant;

/**
 * Represents a  location provider  service. LocationListener will return the response.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class GeoLocationProvider {

  private LocationListener listener;
  private String provider;
  private String packageManager;
  private LocationManager locationManager;
  private Context context;
  private ProviderType type;

  public GeoLocationProvider(LocationListener listener, Context context, ProviderType type) {
    this.listener = listener;
    this.context = context;
    this.type = type;
    switch (type) {
      case NETWORK:
        provider = LocationManager.NETWORK_PROVIDER;
        packageManager = PackageManager.FEATURE_LOCATION_NETWORK;
        break;
      case GPS:
        provider = LocationManager.GPS_PROVIDER;
        packageManager = PackageManager.FEATURE_LOCATION_GPS;
        break;
      case PASSIVE:
        provider = LocationManager.PASSIVE_PROVIDER;
        break;

    }
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  public void fetchLocationOnce() throws LocationException {
    if (hasSystemFeature() && isProviderEnabled() && isSecure()) {
      try {
        locationManager.requestSingleUpdate(provider, listener, null);
        return;
      } catch (SecurityException se) {
        Log.wtf(GeoLocationProvider.class.getSimpleName(), se);
        throw new LocationException(se);
      }
    } else {
      throw new LocationException(hasSystemFeature(), isProviderEnabled(), isSecure());
    }

  }

  public void fetchLocation() throws LocationException {
    if (hasSystemFeature() && isProviderEnabled() && isSecure()) {
      try {
        locationManager.requestLocationUpdates(provider, DeviceConstant.MIN_TIME_BW_UPDATES,
            DeviceConstant.MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
        return;
      } catch (SecurityException se) {
        Log.wtf(GeoLocationProvider.class.getSimpleName(), se);
        throw new LocationException(se);
      }
    } else {
      throw new LocationException(hasSystemFeature(), isProviderEnabled(), isSecure());
    }

  }

  public void removeUpdatedLocation() {
    locationManager.removeUpdates(listener);
  }

  private boolean isProviderEnabled() {
    return locationManager.isProviderEnabled(provider);
  }

  private boolean hasSystemFeature() {
    return type == ProviderType.PASSIVE ||
        this.context.getPackageManager().hasSystemFeature(packageManager);
  }

  private boolean isSecure() {
    boolean isSecure = false;
    switch (this.type) {
      case NETWORK:
        isSecure = ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
        break;
      case GPS:
        isSecure = ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
        break;
      case PASSIVE:
        isSecure = true;
        break;

    }
    return isSecure;
  }

  public enum ProviderType {
    PASSIVE,
    NETWORK,
    GPS
  }

}
