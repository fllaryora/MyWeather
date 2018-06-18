package training.globant.myweather.data.database;

import android.content.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import training.globant.myweather.data.database.dao.ForecastDAO;
import training.globant.myweather.data.database.dao.WeatherDAO;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItem;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.presentation.show_weather.model.IconMapper;

public class DatabaseProvider {

  private WeatherDAO weatherDAO;

  private ForecastDAO forecastDAO;

  public DatabaseProvider(Context context) {
    this.weatherDAO = AppDatabase.getAppDatabase(context).getWeatherDAO();
    this.forecastDAO = AppDatabase.getAppDatabase(context).getForecastDAO();
  }

  public void insert(final Map<String, String> parameters, WeatherInfo weatherInfo) {

    Weather weather = new Weather();
    weather.setTextSubmited(parameters.get(Constant.API_PARAMETER_QUERY));
    weather.setLongitude(parameters.get(Constant.API_PARAMETER_LONGITUDE));
    weather.setLatitude(parameters.get(Constant.API_PARAMETER_LATITUDE));
    weather.setCityLabel(weatherInfo.getName());
    weather.setCountryLabel(weatherInfo.getCountry().getName());
    weather.setTemperatureLabel(weatherInfo.getTemperatureInfo().getTemperature());
    weather.setMaxTemperatureLabel(weatherInfo.getTemperatureInfo().getMaximum());
    weather.setMinTemperatureLabel(weatherInfo.getTemperatureInfo().getMinimum());
    weather.setSkyLabel(weatherInfo.getSkyDescription().get(0).getDescription());
    weather.setIcon( IconMapper.fromInt(weatherInfo.getSkyDescription().get(0).getId()).getIcon());

    weather.setId(0);
    weather.setLastRefresh( new Date() );
    weatherDAO.insert(weather);
  }


  public void insert(final Map<String, String> parameters, ForecastInfo forecastInfo) {
    Forecast forecast = new Forecast();
    forecast.setTextSubmited(parameters.get(Constant.API_PARAMETER_QUERY));
    forecast.setLongitude(parameters.get(Constant.API_PARAMETER_LONGITUDE));
    forecast.setLatitude(parameters.get(Constant.API_PARAMETER_LATITUDE));
    forecast.setCityLabel(forecastInfo.getCity().getCityName());
    forecast.setCountryLabel(forecastInfo.getCity().getCountryName());

    forecast.setId(0);
    forecast.setLastRefresh( new Date() );
    long lastId = forecastDAO.insertForecast(forecast);
    for(training.globant.myweather.data.model.ForecastItem forecastItemInfo: forecastInfo.getList()){
      ForecastItem forecastItem = new ForecastItem();
      forecastItem.setId(IconMapper.fromInt(forecastItemInfo.getSkyDescriptions().get(0).getId()).getIcon());
      forecastItem.setTemperatureLabel(forecastItemInfo.getTemperatureInfo().getTemperature());
      forecastItem.setMaxTemperatureLabel(forecastItemInfo.getTemperatureInfo().getMaximum());
      forecastItem.setMinTemperatureLabel(forecastItemInfo.getTemperatureInfo().getMinimum());
      forecastItem.setDateTime(forecastItemInfo.getDateTime());

      forecastItem.setId(0);
      forecastItem.setForecastId(lastId);
      forecastDAO.insertForecastItem(forecastItem);
    }
  }

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

  public Forecast getForecastByLatitudeAndLongitude(String latitude, String longitude) {
    Forecast forecast = null;
    List<Forecast> forecastList = forecastDAO.getForecast();
    //TODO RESEARCH if only livedata can return null list
    //TODO i.e RESEARCH if this if is necesary
    if (forecastList != null) {
      forecastList = filterForcastOutdatedRows(forecastList);
      forecastList = filterForecastByLatidudeAndLongitude(forecastList, latitude, longitude);
      forecast = filterForecastLastMaxRefresh(forecastList);
    }
    forecast.setForecastItems(forecastDAO.getForecastItems(forecast.getId()));
    return forecast;
  }

  public Forecast getForecastByTextSubmited(String text) {
    Forecast forecast = null;
    List<Forecast> forecastList = forecastDAO.getForecast();

    //TODO RESEARCH if only livedata can return null list
    //TODO i.e RESEARCH if this if is necesary
    if (forecastList != null) {
      forecastList = filterForcastOutdatedRows(forecastList);
      forecastList = filterForecastByText(forecastList, text);
      forecast = filterForecastLastMaxRefresh(forecastList);
    }
    forecast.setForecastItems(forecastDAO.getForecastItems(forecast.getId()));
    return forecast;
  }


  private List<Weather> filterOutdatedRows(List<Weather> rowList) {
    List<Weather> filteredList = new ArrayList<Weather>();
    Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);

    for (Weather item : rowList) {
      Date lastRefresh = item.getLastRefresh();
      //validUntil < lastRefresh
      if (validUntil.before(lastRefresh)) {
        filteredList.add(item);
      } else {
        //outdated items
        weatherDAO.delete(item);
      }
    }

    return filteredList;
  }

  private List<Weather> filterByText(List<Weather> rowList, String text) {
    List<Weather> filteredList = new ArrayList<Weather>();
    for (Weather item : rowList) {
      String textSubmited = item.getTextSubmited();
      if (textSubmited != null && text.equals(textSubmited)) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  private Weather filterLastMaxRefresh(List<Weather> rowList) {
    Weather filteredElement = null;
    Date currentDate = null;

    for (Weather item : rowList) {
      Date dateOfCurrentItem = item.getLastRefresh();
      if (currentDate == null) {
        filteredElement = item;
        currentDate = dateOfCurrentItem;
      } else if (dateOfCurrentItem.after(currentDate)) {
        filteredElement = item;
        currentDate = dateOfCurrentItem;
      }
    }

    return filteredElement;
  }

  private List<Weather> filterByLatidudeAndLongitude(List<Weather> rowList, String latitude,
      String longitude) {
    List<Weather> filteredList = new ArrayList<Weather>();
    for (Weather item : rowList) {
      String currentLatitude = item.getLatitude();
      String currentLongitude = item.getLongitude();
      if (latitude.equals(currentLatitude) && longitude.equals(currentLongitude)) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  private List<Forecast> filterForcastOutdatedRows(List<Forecast> rowList) {
    List<Forecast> filteredList = new ArrayList<Forecast>();
    Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);
    for (Forecast item : rowList) {
      Date lastRefresh = item.getLastRefresh();
      //validUntil < lastRefresh
      if (validUntil.before(lastRefresh)) {
        filteredList.add(item);
      } else {
        //outdated items
        List<ForecastItem> forecastItems = forecastDAO.getForecastItems(item.getId());
        //TODO RESEARCH if only livedata can return null list
        //TODO i.e RESEARCH if this if is necesary
        if (forecastItems != null) {
          for(ForecastItem itemOfItem:forecastItems){
            forecastDAO.deleteForecastItem(itemOfItem);
          }
        }
        forecastDAO.deleteForecast(item);
      }
    }

    return filteredList;
  }

  private List<Forecast> filterForecastByText(List<Forecast> rowList, String text) {
    List<Forecast> filteredList = new ArrayList<Forecast>();
    for (Forecast item : rowList) {
      String textSubmited = item.getTextSubmited();
      if (textSubmited != null && text.equals(textSubmited)) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  private Forecast filterForecastLastMaxRefresh(List<Forecast> rowList) {
    Forecast filteredElement = null;
    Date currentDate = null;

    for (Forecast item : rowList) {
      Date dateOfCurrentItem = item.getLastRefresh();
      if (currentDate == null) {
        filteredElement = item;
        currentDate = dateOfCurrentItem;
      } else if (dateOfCurrentItem.after(currentDate)) {
        filteredElement = item;
        currentDate = dateOfCurrentItem;
      }
    }

    return filteredElement;
  }

  private List<Forecast> filterForecastByLatidudeAndLongitude(List<Forecast> rowList, String latitude,
      String longitude) {
    List<Forecast> filteredList = new ArrayList<Forecast>();
    for (Forecast item : rowList) {
      String currentLatitude = item.getLatitude();
      String currentLongitude = item.getLongitude();
      if (latitude.equals(currentLatitude) && longitude.equals(currentLongitude)) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }


}
