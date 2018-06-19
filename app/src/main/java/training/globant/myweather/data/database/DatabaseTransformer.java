package training.globant.myweather.data.database;

import java.util.Date;
import java.util.Map;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItem;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.presentation.show_weather.model.IconMapper;

public class DatabaseTransformer {

  public Weather getDataBaseWeatherFromInfo(final Map<String, String> parameters, WeatherInfo weatherInfo) {
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
    return weather;
  }

  public Forecast getDataBaseForecastFromInfo(final Map<String, String> parameters, ForecastInfo forecastInfo) {
    Forecast forecast = new Forecast();
    forecast.setTextSubmited(parameters.get(Constant.API_PARAMETER_QUERY));
    forecast.setLongitude(parameters.get(Constant.API_PARAMETER_LONGITUDE));
    forecast.setLatitude(parameters.get(Constant.API_PARAMETER_LATITUDE));
    forecast.setCityLabel(forecastInfo.getCity().getCityName());
    forecast.setCountryLabel(forecastInfo.getCity().getCountryName());

    forecast.setId(0);
    forecast.setLastRefresh( new Date() );
    return forecast;
  }

  public ForecastItem getDataBaseForecastItemFromInfo( training.globant.myweather.data.model.ForecastItem forecastItemInfo, long lastId) {
    ForecastItem forecastItem = new ForecastItem();
    forecastItem.setId(IconMapper.fromInt(forecastItemInfo.getSkyDescriptions().get(0).getId()).getIcon());
    forecastItem.setTemperatureLabel(forecastItemInfo.getTemperatureInfo().getTemperature());
    forecastItem.setMaxTemperatureLabel(forecastItemInfo.getTemperatureInfo().getMaximum());
    forecastItem.setMinTemperatureLabel(forecastItemInfo.getTemperatureInfo().getMinimum());
    forecastItem.setDateTime(forecastItemInfo.getDateTime());

    forecastItem.setId(0);
    forecastItem.setForecastId(lastId);
    return forecastItem;
  }

}
