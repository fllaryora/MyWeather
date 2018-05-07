package training.globant.myweather.data.net;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;

/**
 * the same style than https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java
 * The provider of api Created by francisco on 01/05/18.
 */

public class WeatherAPIClient {

  private static String ENDPOINT = "http://api.openweathermap.org/data/2.5/";
  private static Retrofit retrofit = null;
  private static OpenWeatherMap openWeatherMap = null;

  public static Retrofit provideRestClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(ENDPOINT)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }

  public static OpenWeatherMap provideWeatherAPIClient() {
    if (openWeatherMap == null) {
      openWeatherMap = provideRestClient().create(OpenWeatherMap.class);
    }
    return openWeatherMap;
  }

  public interface OpenWeatherMap {
    @Headers("User-Agent: MyWeather-App")
    @GET("weather")
    Call<WeatherInfo> searchWeatherByCity(@Query(Constant.API_PARAMETER_QUERY) String q,
        @Query(Constant.API_PARAMETER_APP_ID) String appId,
        @Query(Constant.API_PARAMETER_TEMPETATURE_UNITS) String temperatureUnits);
  }

}
