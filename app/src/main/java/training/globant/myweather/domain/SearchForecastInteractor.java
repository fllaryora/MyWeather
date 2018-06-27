package training.globant.myweather.domain;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import training.globant.myweather.BuildConfig;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ErrorInfo;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.net.ErrorHelper;
import training.globant.myweather.data.net.NoConnectivityException;
import training.globant.myweather.data.net.WeatherAPIClient;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.sensors.location.LocationException;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.device.utils.DeviceConstant;

/**
 * Represents a use case for searching the forecast weather of the location.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class SearchForecastInteractor {
  private WeatherAPIClient.OpenWeatherMap weatherClient;
  private Retrofit retrofitClient;
  public SearchForecastInteractor(WeatherAPIClient.OpenWeatherMap weatherClient, Retrofit retrofitClient){
    this.weatherClient = weatherClient;
    this.retrofitClient = retrofitClient;
  }

  /**
   * Executes the current use case (SearchWeather).
   *
   * @param parameters description of the location and so on.
   * @param callback Called when an asynchronous rest api call completes.
   */
  public void execute(final Map<String, String> parameters, final WeatherCallback callback) {
    parameters.put( Constant.API_PARAMETER_APP_ID, BuildConfig.APP_ID);
    parameters.put( Constant.API_PARAMETER_TEMPETATURE_UNITS, Constant.API_VALUE_DEGREES_CELSIUS);
    parameters.put( Constant.API_PARAMETER_LANG, Constant.API_VALUE_LANG_SPANISH);
    Call<ForecastInfo> call = weatherClient.searchForecastByOptions(parameters);
    call.enqueue(new Callback<ForecastInfo>() {
      @Override
      public void onResponse(Call<ForecastInfo> call, Response<ForecastInfo> response) {
        if (response.isSuccessful()) {
          callback.onResponse(response.body());
        } else {
          // Error such as resource not found
          ErrorInfo errorResponse = ErrorHelper.parseError(response,retrofitClient);
          callback.onError(
              String.format(Constant.ERROR_MESSAGES_FORMAT, errorResponse.getErrorCode(),
                  errorResponse.getMessage())
          );
        }
      }

      @Override
      public void onFailure(Call<ForecastInfo> call, Throwable t) {
        // Error such as no internet connection
        if(t instanceof NoConnectivityException) {
          callback.onOffline();
        } else {
          callback.onError(t.getMessage());
        }

      }
    });
  }

  /**
   * Executes the current use case (SearchWeather) from locator.
   *
   * @param callback Called when an asynchronous rest api call completes.
   */
  public void executeGPS(PermissionsHelper permissionsHelper, final WeatherCallback callback) {

    LocationListener listener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constant.API_PARAMETER_LATITUDE, String.valueOf(location.getLatitude()));
        parameters.put(Constant.API_PARAMETER_LONGITUDE, String.valueOf(location.getLongitude()));
        callback.onGeolocation(parameters);
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {
      }

      @Override
      public void onProviderEnabled(String s) {
      }

      @Override
      public void onProviderDisabled(String s) {
      }
    };

    try {
      permissionsHelper.getGPSGeoLocationProvider().setListener(listener);
      permissionsHelper.getGPSGeoLocationProvider().fetchLocationOnce();
    } catch (LocationException e) {
      //TODO PASS tO STRING RESOURCE
      callback.onError(String.format(DeviceConstant.FAIL_MESSAGE, e));
    }

  }

}
