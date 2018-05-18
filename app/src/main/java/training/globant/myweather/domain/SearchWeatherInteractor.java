package training.globant.myweather.domain;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.globant.myweather.BuildConfig;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ErrorInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.ErrorHelper;
import training.globant.myweather.data.net.WeatherAPIClient;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.sensors.location.GeoLocationProvider;
import training.globant.myweather.device.sensors.location.LocationException;
import training.globant.myweather.device.utils.DeviceConstant;

/**
 * Represents a use case for searching the weather of the location.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class SearchWeatherInteractor {

  private GeoLocationProvider geoLocationProvider;

  /**
   * Constructor of SearchWeatherInteractor
   * @param geoLocationProvider instance of the geo-location provider
   */
  public SearchWeatherInteractor( GeoLocationProvider geoLocationProvider) {
    this.geoLocationProvider = geoLocationProvider;
  }

  /**
   * Executes the current use case (SearchWeather).
   *
   * @param parameters description of the location and so on.
   * @param callback Called when an asynchronous rest api call completes.
   */
  public void execute(final Map<String, String> parameters, final WeatherCallback callback) {
    WeatherAPIClient.OpenWeatherMap weatherClient = WeatherAPIClient.provideWeatherAPIClient();
    parameters.put( Constant.API_PARAMETER_APP_ID, BuildConfig.APP_ID);
    Call<WeatherInfo> call = weatherClient.searchWeatherByOptions(parameters);
    call.enqueue(new Callback<WeatherInfo>() {
      @Override
      public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
        if (response.isSuccessful()) {
          //TODO cachear aca ???
          callback.onResponse(response.body());
        } else {
          // Error such as resource not found
          ErrorInfo errorResponse = ErrorHelper.parseError(response);
          callback.onError(
              String.format(Constant.ERROR_MESSAGES_FORMAT, errorResponse.getErrorCode(),
                  errorResponse.getMessage())
          );
        }
      }

      @Override
      public void onFailure(Call<WeatherInfo> call, Throwable t) {
        // Error such as no internet connection
        callback.onError(t.getMessage());
      }
    });
  }

  /**
   * Executes the current use case (SearchWeather) from locator.
   *
   * @param parameters description of the location and so on.
   * @param callback Called when an asynchronous rest api call completes.
   */
  public void executeGPS(final Map<String, String> parameters, final WeatherCallback callback) {

    LocationListener listener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        parameters.put(Constant.API_PARAMETER_LATITUDE, String.valueOf(location.getLatitude()));
        parameters.put(Constant.API_PARAMETER_LONGITUDE, String.valueOf(location.getLongitude()));
        execute(parameters, callback);
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
      this.geoLocationProvider.setListener(listener);
      this.geoLocationProvider.fetchLocationOnce();
    } catch (LocationException e) {
      //TODO PASS tO STRING RESOURCE
      callback.onError(String.format(DeviceConstant.FAIL_MESSAGE, e));
    }

  }

}
