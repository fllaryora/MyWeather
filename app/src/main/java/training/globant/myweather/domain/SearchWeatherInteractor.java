package training.globant.myweather.domain;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ErrorInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.ErrorHelper;
import training.globant.myweather.data.net.WeatherAPIClient;

/**
 * Search Weather use cases
 * Created by francisco on 01/05/18.
 */

public class SearchWeatherInteractor {

    public void execute(final Map<String,String> parameters, final WeatherCallback callback){
        //TODO: looking up for cached results here
        //TODO call repository with both parameters
        WeatherAPIClient.OpenWeatherMap weatherClient = WeatherAPIClient.provideWeatherAPIClient();
        String query =  parameters.get("q");
        String apiKey = parameters.get("APPID");
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
