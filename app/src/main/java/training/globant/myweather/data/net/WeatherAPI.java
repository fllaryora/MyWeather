package training.globant.myweather.data.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import training.globant.myweather.data.model.WeatherInfo;

/**
 *
 * Created by francisco on 01/05/18.
 */

public interface WeatherAPI {
    @Headers("User-Agent: MyWeather-App")
    @GET("weather")
    Call<WeatherInfo> searchWeatherByCity(@Query("q") String q, @Query("APPID") String appId);
}
