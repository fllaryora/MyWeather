package training.globant.myweather.data.net;

import android.content.Context;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;

//the same style than https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java

/**
 * Represents a rest Weather API Client
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class WeatherAPIClient {

  private static String ENDPOINT = "http://api.openweathermap.org/data/2.5/";
  private static int CONNECT_TIMEOUT = 30;
  private static int READ_TIMEOUT = 30;
  private static int WRITE_TIMEOUT = 30;
  private static Retrofit retrofit = null;
  private static OkHttpClient client = null;
  private static OpenWeatherMap openWeatherMap = null;


  /**
   * Provides Retrofit instance
   *
   * @return Retrofit instance
   */
  public static OkHttpClient provideClient(Context context) {
    if (client == null) {
      client = new OkHttpClient.Builder()
          .addInterceptor(new NetworkConnectionInterceptor(context))
          .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
          .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
          .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
          .build();
    }
    return client;
  }

  /**
   * Provides Retrofit instance
   *
   * @return Retrofit instance
   */
  public static Retrofit provideRestClient(Context context) {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(ENDPOINT)
          .client(provideClient(context))
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }

  /**
   * Provides an instance of OpenWeatherMap
   *
   * @return an instance of OpenWeatherMap
   */
  public static OpenWeatherMap provideWeatherAPIClient(Context context) {
    if (openWeatherMap == null) {
      openWeatherMap = provideRestClient(context).create(OpenWeatherMap.class);
    }
    return openWeatherMap;
  }

  /**
   * Contains OpenWeatherMap methods
   */
  public interface OpenWeatherMap {

    /**
     * Searches the weather by options
     *
     * @param options is a map that could contain:
     * - q -query
     * - APPID -app id
     * - units - temperatureUnits
     * - lang- language of the description
     * -lat - latitude
     * -lon - longitude
     * @return a callable with WeatherInfo
     */
    @Headers("User-Agent: MyWeather-App")
    @GET("weather")
    Call<WeatherInfo> searchWeatherByOptions(@QueryMap(encoded = true) Map<String, String> options);

    /**
     * Searches the forecast  weather by options
     *
     * @param options is a map that could contain:
     * - q -query
     * - APPID -app id
     * - units - temperatureUnits
     * - lang- language of the description
     * -lat - latitude
     * -lon - longitude
     * @return a callable with WeatherInfo
     */
    @Headers("User-Agent: MyWeather-App")
    @GET("forecast")
    Call<ForecastInfo> searchForecastByOptions(
        @QueryMap(encoded = true) Map<String, String> options);
  }

}
