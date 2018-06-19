package training.globant.myweather.data.database;

import java.util.Map;
import training.globant.myweather.data.model.WeatherInfo;

public class WeatherTransactions {

  public Runnable insertWeather(final AppDatabase database,
      final Map<String, String> parameters, final WeatherInfo weatherInfo) {
    return new Runnable() {
      @Override
      public void run() {
        database.getWeatherDAO().insert(
            new DatabaseTransformer().getDataBaseWeatherFromInfo(parameters, weatherInfo)
        );
      }
    };
  }

/*
  public Weather getWeatherByLatitudeAndLongitude(String latitude, String longitude) {
    Weather weather = null;
    List<Weather> weatherList = weatherDAO.getWeathers();
    //TODO RESEARCH if only livedata can return null list
    //TODO i.e RESEARCH if this if is necesary
    if (weatherList != null) {
      weatherList = filterOutdatedRows(weatherList);
      weatherList = filterByLatidudeAndLongitude(weatherList, latitude, longitude);
      weather = filterLastMaxRefresh(weatherList);
    }

    return weather;
  }

  public Weather getWeatherByTextSubmited(String text) {
    Weather weather = null;
    List<Weather> weatherList = weatherDAO.getWeathers();

    //TODO RESEARCH if only livedata can return null list
    //TODO i.e RESEARCH if this if is necesary
    if (weatherList != null) {
      weatherList = filterOutdatedRows(weatherList);
      weatherList = filterByText(weatherList, text);
      weather = filterLastMaxRefresh(weatherList);
    }

    return weather;
  }
*/

}
