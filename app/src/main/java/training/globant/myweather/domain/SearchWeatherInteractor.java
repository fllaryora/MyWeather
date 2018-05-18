package training.globant.myweather.domain;

import static training.globant.myweather.data.utils.Constant.ERROR_MESSAGES_FORMAT;

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

/**
 * Represents a use case for searching the weather of the location.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class SearchWeatherInteractor {

  /**
   * Executes the current use case (SearchWeather).
   *
   * @param parameters description of the location and so on.
   * @param callback Called when an asynchronous rest api call completes.
   */
  public void execute(final Map<String, String> parameters, final WeatherCallback callback) {
    //TODO: looking up for cached results here
    //TODO call repository with both parameters
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
              String.format(ERROR_MESSAGES_FORMAT, errorResponse.getErrorCode(),
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

}
