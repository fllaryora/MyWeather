package training.globant.myweather.data.database.runnable;

import java.util.List;
import java.util.Map;
import training.globant.myweather.data.database.AppDatabase;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItemDB;
import training.globant.myweather.data.database.filters.ForecastFilter;
import training.globant.myweather.data.database.transformer.ForecastTransformer;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.ForecastItem;
import training.globant.myweather.data.utils.Constant;

public class ForecastRunnable {

  private ForecastTransformer transformer;
  private ForecastFilter filter;

  public ForecastRunnable(){
    transformer = new ForecastTransformer();
    filter = new ForecastFilter();
  }

  public Runnable getMyInsertRunnable(final AppDatabase database,
      final Map<String, String> parameters, final ForecastInfo forecastInfo) {
    return new Runnable() {
      @Override
      public void run() {
        long lastId = database.getForecastDAO().insert(
            transformer.getDataBaseForecastFromInfo(parameters, forecastInfo)
        );
        for(ForecastItem item: forecastInfo.getList()){
          database.getForecastDAO().insertItem(
              transformer.getDataBaseForecastItemFromInfo(item, lastId)
          );
        }
      }
    };
  }

  public Runnable getWeatherByParameters(final AppDatabase database,
      final Map<String, String> parameters, final List<ForecastInfo> forecastInfoWrapper) {

    return new Runnable() {
      @Override
      public void run() {
        ForecastInfo forecastInfo = null;
        Forecast forecast = null;
        forecastInfoWrapper.clear();

        List<Forecast> forecastList = database.getForecastDAO().getForecast();
        //TODO RESEARCH if only livedata can return null list
        //TODO i.e RESEARCH if this if is necesary
        if (forecastList != null) {

          forecastList = filter.filterByValidUntil(forecastList);
          String query = parameters.get(Constant.API_PARAMETER_QUERY);
          if (query != null){
            forecastList = filter.filterByText(forecastList, query);
          } else {
            String latitude = parameters.get(Constant.API_PARAMETER_LATITUDE);
            String longitude = parameters.get(Constant.API_PARAMETER_LONGITUDE);
            forecastList = filter.filterByLatidudeAndLongitude(forecastList, latitude, longitude);
          }
          forecast = filter.filterByLastRefresh(forecastList);
          if(forecast != null){

            List<ForecastItemDB> forecastItemList =
                database.getForecastDAO().getForecastItems(forecast.getId());

            forecastInfo = transformer.getInfoForecastFromDataBase(forecast, forecastItemList);
            forecastInfoWrapper.add(forecastInfo);
          }
        }
      }
    };
  }

}
