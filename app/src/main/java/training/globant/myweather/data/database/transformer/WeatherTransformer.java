package training.globant.myweather.data.database.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.presentation.show_weather.model.IconMapper;

public class WeatherTransformer {
  public Weather getDataBaseWeatherFromInfo(final Map<String, String> parameters, WeatherInfo weatherInfo) {
    Weather weather = new Weather();
    weather.setTextSubmited(parameters.get(Constant.API_PARAMETER_QUERY));
    weather.setLongitude(parameters.get(Constant.API_PARAMETER_LONGITUDE));
    weather.setLatitude(parameters.get(Constant.API_PARAMETER_LATITUDE));
    weather.setCityLabel(weatherInfo.getName());
    weather.setCountryLabel(weatherInfo.getCountry().getName());
    weather.setTemperature(weatherInfo.getTemperatureInfo().getTemperature());
    weather.setMaxTemperature(weatherInfo.getTemperatureInfo().getMaximum());
    weather.setMinTemperature(weatherInfo.getTemperatureInfo().getMinimum());
    weather.setSkyLabel(weatherInfo.getSkyDescription().get(0).getDescription());
    weather.setIcon( weatherInfo.getSkyDescription().get(0).getId());

    weather.setId(0);
    weather.setLastRefresh( new Date() );
    return weather;
  }

  public WeatherInfo getInfoWeatherFromDataBase( Weather weather) {
    CountryHolder country = new CountryHolder(weather.getCountryLabel());
    TemperatureInfo temperatureInfo = new TemperatureInfo(weather.getTemperature(),
        weather.getMinTemperature(), weather.getMaxTemperature());
    List<SkyDescription> skyDescriptionList = new ArrayList<>();
    SkyDescription skyDescription = new SkyDescription(weather.getIcon(), "", weather.getSkyLabel());
    skyDescriptionList.add(skyDescription);
    return new  WeatherInfo(weather.getCityLabel(), skyDescriptionList,
        temperatureInfo, country);
  }

}
