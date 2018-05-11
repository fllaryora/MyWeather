package training.globant.myweather.data.net;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;

//the same style than https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java
/**
 * Represents a rest Weather API Client
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 * */

public class WeatherAPIClient {

  private static String ENDPOINT = "http://api.openweathermap.org/data/2.5/";
  private static Retrofit retrofit = null;
  private static OpenWeatherMap openWeatherMap = null;

  /**
   * Provides Retrofit instance
   * @return Retrofit instance
   */
  public static Retrofit provideRestClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(ENDPOINT)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }

  /**
   * Provides an instance of OpenWeatherMap
   * @return an instance of OpenWeatherMap
   */
  public static OpenWeatherMap provideWeatherAPIClient() {
    if (openWeatherMap == null) {
      openWeatherMap = provideRestClient().create(OpenWeatherMap.class);
    }
    return openWeatherMap;
  }

  /**
   * Contains OpenWeatherMap methods
   */
  public interface OpenWeatherMap {

    /**
     * Searches the weather by city
     * @param q query
     * @param appId app id
     * @param temperatureUnits
     * @param lang language of the description
     * @return a callable with WeatherInfo
     */
    @Headers("User-Agent: MyWeather-App")
    @GET("weather")
    Call<WeatherInfo> searchWeatherByCity(@Query(Constant.API_PARAMETER_QUERY) String q,
        @Query(Constant.API_PARAMETER_APP_ID) String appId,
        @Query(Constant.API_PARAMETER_TEMPETATURE_UNITS) String temperatureUnits,
        @Query(Constant.API_PARAMETER_LANG) String lang);
  }

}
