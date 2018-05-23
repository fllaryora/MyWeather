package training.globant.myweather.data;

import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;

/**
 * Represents a callback for a rest API weather service. If an API call is successful, then
 * onResponse(WeatherInfo) is called, otherwise onError(String) is called.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public interface WeatherCallback {

  /**
   * Called when an asynchronous call completes successfully.
   *
   * @param weatherInfo data model of the weather
   */
  void onResponse(WeatherInfo weatherInfo);

  /**
   * Called when an asynchronous call completes successfully.
   *
   * @param weatherInfo data model of the weather
   */
  void onResponse(ForecastInfo weatherInfo);

  /**
   * Called when an asynchronous call fails to complete normally.
   *
   * @param error description
   */
  void onError(String error);
}
