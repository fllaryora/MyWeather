package training.globant.myweather.device;

import android.support.annotation.NonNull;

/**
 * Represents a callback for permissions service.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public interface PermissionHelperCallback {

  /**
   * Called when you don't find GPS permissions.
   */
  void onRequestPermissionsResultFail(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults);

  /**
   * Called when you have GPS permissions.
   */
  void onResponse();

  /**
   * Called when you don't have permissions.
   */
  void onPermissionDenied();


  /**
   * Called when you don't have GPS.
   */
  void onHasntSystemFeature();

  /**
   * Called when you have to show an explanation about permissions.
   */
  void onShowAnExplanation();

}
