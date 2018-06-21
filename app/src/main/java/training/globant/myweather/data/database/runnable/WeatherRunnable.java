package training.globant.myweather.data.database.runnable;

import java.util.List;
import java.util.Map;
import training.globant.myweather.data.database.AppDatabase;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.database.filters.WeatheFilter;
import training.globant.myweather.data.database.transformer.WeatherTransformer;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;

public class WeatherRunnable {

  private WeatherTransformer transformer;
  private WeatheFilter filter;

  public WeatherRunnable(){
    transformer = new WeatherTransformer();
    filter = new WeatheFilter();
  }

  public Runnable getMyInsertRunnable(final AppDatabase database,
      final Map<String, String> parameters, final WeatherInfo weatherInfo) {
    return new Runnable() {
      @Override
      public void run() {
        database.getWeatherDAO().insert(
            transformer.getDataBaseWeatherFromInfo(parameters, weatherInfo)
        );
      }
    };
  }


  public Runnable getWeatherByParameters(final AppDatabase database,
      final Map<String, String> parameters, final List<WeatherInfo> weatherInfoWrapper) {

    return new Runnable() {
      @Override
      public void run() {
        WeatherInfo weatherInfo = null;
        Weather weather = null;
        weatherInfoWrapper.clear();

        List<Weather> weatherList = database.getWeatherDAO().getWeathers();
        //TODO RESEARCH if only livedata can return null list
        //TODO i.e RESEARCH if this if is necesary
        if (weatherList != null) {

          weatherList = filter.filterByValidUntil(weatherList);
          String query = parameters.get(Constant.API_PARAMETER_QUERY);
          if (query != null){
            weatherList = filter.filterByText(weatherList, query);
          } else {
            String latitude = parameters.get(Constant.API_PARAMETER_LATITUDE);
            String longitude = parameters.get(Constant.API_PARAMETER_LONGITUDE);
            weatherList = filter.filterByLatidudeAndLongitude(weatherList, latitude, longitude);
          }
          weather = filter.filterByLastRefresh(weatherList);
          if(weather != null){
            weatherInfo = transformer.getInfoWeatherFromDataBase(weather);
            weatherInfoWrapper.add(weatherInfo);
          }
        }
      }
    };
  }

}
