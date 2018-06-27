package training.globant.myweather.data.database.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItemDB;
import training.globant.myweather.data.model.City;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.ForecastItem;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.utils.Constant;

public class ForecastTransformer {

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

  public ForecastItemDB getDataBaseForecastItemFromInfo( ForecastItem forecastItemInfo, long lastId) {
    ForecastItemDB forecastItem = new ForecastItemDB();
    forecastItem.setIcon(forecastItemInfo.getSkyDescriptions().get(0).getId());
    forecastItem.setTemperature(forecastItemInfo.getTemperatureInfo().getTemperature());
    forecastItem.setMaxTemperature(forecastItemInfo.getTemperatureInfo().getMaximum());
    forecastItem.setMinTemperature(forecastItemInfo.getTemperatureInfo().getMinimum());
    forecastItem.setDateTime(forecastItemInfo.getDateTime());

    forecastItem.setId(0);
    forecastItem.setForecastId(lastId);
    return forecastItem;
  }

  public ForecastItem getItemInfoForecastItemFromDatabase( ForecastItemDB forecastItemInfo) {
    TemperatureInfo temperatureInfo = new TemperatureInfo(forecastItemInfo.getTemperature(),
        forecastItemInfo.getMinTemperature(), forecastItemInfo.getMaxTemperature());
    List<SkyDescription> skyDescriptionList = new ArrayList<>();
    SkyDescription skyDescription = new SkyDescription(forecastItemInfo.getIcon(), "", "");
    skyDescriptionList.add(skyDescription);

    return new ForecastItem(forecastItemInfo.getDateTime(), temperatureInfo, skyDescriptionList);

  }

  public ForecastInfo getInfoForecastFromDataBase(Forecast forecast, List<ForecastItemDB> forecastItemList) {
    City city = new City(forecast.getCityLabel(), forecast.getCountryLabel()) ;
    List<ForecastItem> list = new ArrayList<ForecastItem>();
    for(ForecastItemDB bdItem : forecastItemList){
      list.add(getItemInfoForecastItemFromDatabase( bdItem ));
    }
    return new ForecastInfo(city, list.size(), list) ;
  }

}
