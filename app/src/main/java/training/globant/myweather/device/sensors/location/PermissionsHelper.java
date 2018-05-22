package training.globant.myweather.device.sensors.location;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import training.globant.myweather.R;
import training.globant.myweather.device.PermissionHelperCallback;
import training.globant.myweather.device.utils.DeviceConstant;
import training.globant.myweather.device.utils.DeviceConstant.RequestCodes;

/**
 * Represents a wrapper of location provider for Fragment.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */


public class PermissionsHelper {

  private final Fragment fragment;
  private GeoLocationProvider geoLocationProvider;

  public PermissionsHelper(Fragment fragment) {
    this.fragment = fragment;
    this.geoLocationProvider = new GeoLocationProvider(this.fragment.getContext(), GeoLocationProvider.ProviderType.GPS);
  }

  /**
   * Returns a GPS GeoLocation Provider for presenters
   * @return a GPS GeoLocation Provider
   */
  public GeoLocationProvider getGPSGeoLocationProvider() {
    return geoLocationProvider;
  }

  /**
   * Call callback if you have permissions
   */
  public void tryLocation(PermissionHelperCallback callback) {
    if (hasSystemGPS()) {
      if(isProviderEnabled()){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && validatePermission()) {
          if (shouldWeShowAnExplanation()) {
            Toast.makeText(this.fragment.getContext(), R.string.explanation, Toast.LENGTH_SHORT).show();
          }
          requestPermissionsToUser();
          //The callback method (onRequestPermissionsResult) gets the result of the request.
          return;
        }
        callback.onResponse();
      } else {
        displayPromptForEnablingGPS();
      }
    }
  }

  private boolean validatePermission() {
    return ActivityCompat
        .checkSelfPermission(this.fragment.getContext(), permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat
        .checkSelfPermission(this.fragment.getContext(), permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED;
  }

  private boolean shouldWeShowAnExplanation() {
    return this.fragment.shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_COARSE_LOCATION) &&
        this.fragment.shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_FINE_LOCATION);
  }

  private void requestPermissionsToUser() {
    this.fragment.requestPermissions(DeviceConstant.accessLocations,
        RequestCodes.REQUEST_GPS.getRequestCode()
    );
  }

  private boolean isProviderEnabled() {
    LocationManager locationManager = (LocationManager) this.fragment.getActivity()
        .getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  private boolean hasSystemGPS() {
    return this.fragment.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
  }

  private void displayPromptForEnablingGPS() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this.fragment.getActivity());
    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
    final String message = this.fragment.getString(R.string.enable_gps_message);

    builder.setMessage(message)
        .setPositiveButton(this.fragment.getString(R.string.ok),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface d, int id) {
                fragment.getActivity().startActivity(new Intent(action));
                d.dismiss();
              }
            });

    builder.create().show();
  }

  /**
   * Call callback if you have permissions
   */
  public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults, PermissionHelperCallback permission) {
    if (requestCode == RequestCodes.REQUEST_GPS.getRequestCode()) {
      boolean permissionGranted = true;

      for(int i = 0; i < permissions.length ; i++){
        if(DeviceConstant.isAccessInAccessLocation(permissions[i])
            && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
          permissionGranted = false;
          break;
        }
      }

      if(permissionGranted){
        permission.onResponse();
      }
    } else {
      permission.onError();
    }
  }

}
