package training.globant.myweather.data.net;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The provider of api
 * Created by francisco on 01/05/18.
 */

public class WeatherClient {

    private static String ENDPOINT = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;
    private static WeatherAPI weatherAPI = null;

    public static Retrofit provideRestClient() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static WeatherAPI provideWeatherAPIClient() {
        if(weatherAPI == null){
            weatherAPI = provideRestClient().create(WeatherAPI.class);
        }
        return weatherAPI;
    }

}
