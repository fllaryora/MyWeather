package training.globant.myweather.data.database;

public class ForecastTransactions {

/*
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
  */

}
