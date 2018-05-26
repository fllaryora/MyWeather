package training.globant.myweather.presentation.show_forecast.presenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.ForecastItem;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchForecastInteractor;
import training.globant.myweather.presentation.show_forecast.ShowForecastContract;
import training.globant.myweather.presentation.show_forecast.model.CityUI;
import training.globant.myweather.presentation.show_forecast.model.ForecastItemUI;
import training.globant.myweather.presentation.show_weather.model.IconMapper;

/**
 * {@link ShowForecastContract.Presenter} that controls communication between views and view models
 * of the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowForecastPresenter implements ShowForecastContract.Presenter, WeatherCallback {

  private ShowForecastContract.View view;
  private CityUI uiModel;

  /**
   * Holds ShowWeatherContract.View view in a member
   *
   * @param view instance
   */
  @Override
  public void attachView(ShowForecastContract.View view) {
    this.view = view;
  }

  /**
   * Returns true if the view is attached
   *
   * @return true if the view is attached
   */
  @Override
  public boolean isViewAttached() {
    return view != null;
  }

  /**
   * Dettaches the view
   */
  @Override
  public void dettachView() {
    this.view = null;
  }

  /**
   * Load the forecast using parameters
   *
   * @param parameters pair key-value data that describe a location
   */
  @Override
  public void loadForecast(Map<String, String> parameters) {
    SearchForecastInteractor searchWeatherInteractor = new SearchForecastInteractor();
    if (parameters == null) {
      searchWeatherInteractor.executeGPS(view.getPermissionHelper(), this);
    } else {
      String query = parameters.get(Constant.API_PARAMETER_QUERY);
      if (isQueryValid(query)) {
        searchWeatherInteractor.execute(parameters, this);
      } else {
        if (isViewAttached()) {
          view.showError(view.getInvalidQueryString());
        }
      }
    }
  }

  private boolean isQueryValid(String query) {
    if (query == null) {
      return false;
    }
    query = query.trim();
    return !query.isEmpty() && query.length() > Constant.MIN_QUERY_LEN;
  }

  /**
   * Transforms one data model into view model
   *
   * @param model data model instance
   * @return view model instance
   */
  @Override
  public CityUI transformModelToUiModel(ForecastInfo model) {
    if (model == null) {
      return null;
    }
    String cityName = model.getCity().getCityName();
    List<ForecastItem> description = model.getList();
    List<ForecastItemUI> forecastListUI = new ArrayList<ForecastItemUI>();
    for (ForecastItem forecastItem : description) {
      forecastListUI.add(transformModelToUiModel(forecastItem));
    }
    return new CityUI(cityName, forecastListUI);
  }

  public ForecastItemUI transformModelToUiModel(ForecastItem model) {
    if (model == null) {
      return null;
    }
    int icon = IconMapper.fromInt(model.getSkyDescriptions().get(0).getId()).getIcon();
    String temperatureInfo = getTemperatueFormatted(model.getTemperatureInfo().getTemperature());
    String maxTemperatureInfo = getTemperatueFormatted(model.getTemperatureInfo().getMaximum());
    String minTemperatureInfo = getTemperatueFormatted(model.getTemperatureInfo().getMinimum());
    String dayLabel = getDayOfWeekFormatted(model.getDateTime());
    String hourLabel = getHourFormatted(model.getDateTime());
    return new ForecastItemUI(dayLabel, hourLabel, temperatureInfo, maxTemperatureInfo,
        minTemperatureInfo, icon);
  }

  private String getTemperatueFormatted(double temperature) {
    //https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setDecimalSeparator(Constant.DECIMAL_SEPARATOR);
    DecimalFormat formatter = new DecimalFormat(Constant.DECIMAL_FORMAT_PATTERN, symbols);
    return formatter.format(temperature);
  }

  private String getDayOfWeekFormatted(long UTCDateTime) {
    Calendar cal = Calendar.getInstance();
    TimeZone tz = cal.getTimeZone();
    SimpleDateFormat formatter = new SimpleDateFormat(Constant.DAY_OF_WEEK_FORMAT_PATTERN,
        Locale.getDefault());
    formatter.setTimeZone(tz);
    return formatter.format(new Date(UTCDateTime * 1000));
  }

  private String getHourFormatted(long UTCDateTime) {
    Calendar cal = Calendar.getInstance();
    TimeZone tz = cal.getTimeZone();
    SimpleDateFormat formatter = new SimpleDateFormat(Constant.HOURS_IN_24_FORMAT_PATTERN,
        Locale.getDefault());
    formatter.setTimeZone(tz);
    return formatter.format(new Date(UTCDateTime * 1000));
  }

  /**
   * Returns the view model
   *
   * @return view model instance
   */
  @Override
  public CityUI getUiModel() {
    return uiModel;
  }

  /**
   * Restores the state and show Forecast model.
   * Shows the view model if it exists.
   *
   * @param uiModel view model
   */
  @Override
  public void restoreStateAndShowForecast(CityUI uiModel) {
    if (isViewAttached()) {
      if (uiModel != null) {
        this.uiModel = uiModel;
        view.showForecast(uiModel);
      }
    }
  }

  @Override
  public void onResponse(WeatherInfo weatherInfo) {
    //kepp empty
  }

  /**
   * Connect the data model to the view after a successful response
   *
   * @param forecastInfo data model of the forecast
   */
  @Override
  public void onResponse(ForecastInfo forecastInfo) {
    if (isViewAttached()) {
      uiModel = transformModelToUiModel(forecastInfo);
      view.showForecast(uiModel);
    }
  }

  /**
   * Connect the data model to the view after a failure response
   *
   * @param error description
   */
  @Override
  public void onError(String error) {
    if (isViewAttached()) {
      view.showError(error);
    }
  }

  /**
   * Refresh the weather loading the weather again using lastParameters
   *
   * @param lastParameters pair key-value data that describe a location
   */
  @Override
  public void refreshForecast(Map<String, String> lastParameters) {
    if (isViewAttached()) {
      if (lastParameters != null && !lastParameters.isEmpty()) {
        //TODO ADD CONTROL CACHE TO BE SURE THAT WE HAVE A NETWORK RESPONSE
        loadForecast(lastParameters);
      } else {
        view.stopRefreshing();
      }
    }
  }

}
