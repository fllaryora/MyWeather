package training.globant.myweather.data.database.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.utils.Constant;

public class ForecastFilter {

    public List<Forecast> filterByText(List<Forecast> rowList, String text) {
      List<Forecast> filteredList = new ArrayList<Forecast>();
      for (Forecast item : rowList) {
        String textSubmited = item.getTextSubmited();
        if (textSubmited != null && text.equals(textSubmited)) {
          filteredList.add(item);
        }
      }
      return filteredList;
    }

    public Forecast filterByLastRefresh(List<Forecast> rowList) {
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

    public List<Forecast> filterByLatidudeAndLongitude(List<Forecast> rowList, String latitude,
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

    public List<Forecast> filterByValidUntil(List<Forecast> rowList) {
      List<Forecast> filteredList = new ArrayList<Forecast>();
      Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);
      for (Forecast item : rowList) {
        Date lastRefresh = item.getLastRefresh();
        //validUntil < lastRefresh
        if (validUntil.before(lastRefresh)) {
          filteredList.add(item);
        }
      }

      return filteredList;
    }

    public List<Forecast> filterByOutdatedRows(List<Forecast> rowList) {
      List<Forecast> outdatedList = new ArrayList<Forecast>();
      Date validUntil = new Date(System.currentTimeMillis() - Constant.FOUR_HOUR_AGO);
      for (Forecast item : rowList) {
        Date lastRefresh = item.getLastRefresh();
        //validUntil > lastRefresh
        if (validUntil.after(lastRefresh)) {
          outdatedList.add(item);
        }
      }
      return outdatedList;
    }

}
