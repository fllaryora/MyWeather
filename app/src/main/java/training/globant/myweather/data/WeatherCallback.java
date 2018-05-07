package training.globant.myweather.data;

import training.globant.myweather.data.model.WeatherInfo;

/**
 * Callback for weather response or error.
 * Created by francisco on 01/05/18.
 */

public interface WeatherCallback {

  void onResponse(WeatherInfo weatherInfo);

  void onError(String error);
}
