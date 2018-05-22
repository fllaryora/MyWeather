package training.globant.myweather.device;

/**
 * Represents a callback for permissions service.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public interface PermissionHelperCallback {
  /**
   * Called when you have permissions.
   */
  void onResponse();

  /**
   * Called when you don't have permissions.
   */
  void onError();

}
