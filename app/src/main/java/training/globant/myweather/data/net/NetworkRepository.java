package training.globant.myweather.data.net;

import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ErrorInfo;
import training.globant.myweather.data.model.WeatherInfo;

/**
 * Created by francisco on 01/05/18.
 */

public class NetworkRepository {

    public void searchWeatherByCity(final Map parameters, final WeatherCallback callback) {
        WeatherAPI weatherClient = WeatherClient.provideWeatherAPIClient();
        String query = (String) parameters.get("q");
        String apiKey = (String) parameters.get("APPID");
        Call<WeatherInfo> call = weatherClient.searchWeatherByCity(query, apiKey);
        call.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.isSuccessful()) {
                    //TODO cachear aca ???
                    callback.onResponse(response.body());
                } else {
                    // Error such as resource not found
                    ErrorInfo errorResponse = ErrorHelper.parseError(response);
                    callback.onError( errorResponse.getErrorCode() + " "+ errorResponse.getMessage());
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
