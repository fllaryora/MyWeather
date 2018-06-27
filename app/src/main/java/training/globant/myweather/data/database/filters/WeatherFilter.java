package training.globant.myweather.data.database.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.utils.Constant;

public class WeatherFilter {

  public List<Weather> filterByText(List<Weather> rowList, String text) {
    List<Weather> filteredList = new ArrayList<Weather>();
    for (Weather item : rowList) {
      String textSubmited = item.getTextSubmited();
      if (textSubmited != null && text.equals(textSubmited)) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  public Weather filterByLastRefresh(List<Weather> rowList) {
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

  public List<Weather> filterByLatidudeAndLongitude(List<Weather> rowList, String latitude,
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

  public List<Weather> filterByValidUntil(List<Weather> rowList) {
    List<Weather> filteredList = new ArrayList<Weather>();
    Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);
    for (Weather item : rowList) {
      Date lastRefresh = item.getLastRefresh();
      //validUntil < lastRefresh
      if (validUntil.before(lastRefresh)) {
        filteredList.add(item);
      }
    }

    return filteredList;
  }

  public List<Weather> filterByOutdatedRows(List<Weather> rowList) {
    List<Weather> outdatedList = new ArrayList<Weather>();
    Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);

    for (Weather item : rowList) {
      Date lastRefresh = item.getLastRefresh();
      //validUntil > lastRefresh
      if (validUntil.after(lastRefresh)) {
        outdatedList.add(item);
      }
    }
    return outdatedList;
  }

}
