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
import retrofit2.Retrofit;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.database.AppDatabase;
import training.globant.myweather.data.database.DatabaseHandler;
import training.globant.myweather.data.database.entities.Forecast;
import training.globant.myweather.data.database.entities.ForecastItemDB;
import training.globant.myweather.data.database.filters.ForecastFilter;
import training.globant.myweather.data.database.transformer.ForecastTransformer;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.ForecastItem;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.WeatherAPIClient;
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

public class ShowForecastPresenter implements ShowForecastContract.Presenter, WeatherCallback, DatabaseHandler.Callback {

  private ShowForecastContract.View view;
  private CityUI uiModel;
  private final List<ForecastInfo> forecastInfoWrapper;
  private DatabaseHandler databaseHandler;
  private AppDatabase database;
  private SearchForecastInteractor searchForecastInteractor;
  private Map<String,String> lastParameters;
  private ForecastTransformer transformer;
  private ForecastFilter filter;

  public ShowForecastPresenter(AppDatabase database, WeatherAPIClient.OpenWeatherMap weatherClient, Retrofit retrofitClient){
    this.searchForecastInteractor = new SearchForecastInteractor(weatherClient, retrofitClient);
    databaseHandler = new DatabaseHandler(database, this);
    this.database = database;
    forecastInfoWrapper = new ArrayList<ForecastInfo>();
    databaseHandler = new DatabaseHandler(database, this);
    transformer = new ForecastTransformer();
    filter = new ForecastFilter();
  }

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
    if (!hasParametersAQuery(parameters)) {
      searchForecastInteractor.executeGPS(view.getPermissionHelper(), this);
    } else {
      String query = parameters.get(Constant.API_PARAMETER_QUERY);
      if (isQueryValid(query)) {
        onGeolocation(parameters);
      } else {
        if (isViewAttached()) {
          view.showError(view.getInvalidQueryString());
        }
      }
    }
  }

  /**
   * Called when parameters are ready to be localized by api
   * @param parameters
   */
  private void doRequest(final Map<String, String> parameters) {
    lastParameters = parameters;
    searchForecastInteractor.execute(lastParameters, this);
  }

  /**
   * actions taken After being geo-localized
   * @param parameters with coordinates
   */
  @Override
  public void onGeolocation(final Map<String, String> parameters) {
    doRequest(parameters);
  }

  private boolean hasParametersAQuery(Map<String, String> parameters) {
    if (parameters == null) {
      return false;
    }
    String query = parameters.get(Constant.API_PARAMETER_QUERY);
    if (query == null) {
      return false;
    }
    return true;
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
    String countryName = model.getCity().getCountryName();
    List<ForecastItem> description = model.getList();
    List<ForecastItemUI> forecastListUI = new ArrayList<ForecastItemUI>();
    for (ForecastItem forecastItem : description) {
      forecastListUI.add(transformModelToUiModel(forecastItem));
    }
    return new CityUI(cityName, forecastListUI , countryName);
  }

  public ForecastItemUI transformModelToUiModel(ForecastItem model) {
    if (model == null) {
      return null;
    }
    int icon = IconMapper.fromInt(model.getSkyDescriptions().get(0).getId()).getIcon();
    String temperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getTemperature());
    String maxTemperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getMaximum());
    String minTemperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getMinimum());
    String dayLabel = getDayOfWeekFormatted(model.getDateTime());
    String hourLabel = getHourFormatted(model.getDateTime());
    return new ForecastItemUI(dayLabel, hourLabel, temperatureInfo, maxTemperatureInfo,
        minTemperatureInfo, icon);
  }

  private String getTemperatureFormatted(double temperature) {
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
  public void restoreStateAndShowForecast(CityUI uiModel, boolean isValid) {
    if(!isValid){
      uiModel = null;
      this.uiModel = null;
    }
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
  public void onResponse(final ForecastInfo forecastInfo) {
    forecastInfoWrapper.clear();
    forecastInfoWrapper.add(forecastInfo);
    databaseHandler.execute(
        new Runnable() {
          @Override
          public void run() {
            long lastId = database.getForecastDAO().insert(
                transformer.getDataBaseForecastFromInfo(lastParameters, forecastInfo)
            );
            for(ForecastItem item: forecastInfo.getList()){
              database.getForecastDAO().insertItem(
                  transformer.getDataBaseForecastItemFromInfo(item, lastId)
              );
            }
          }
        }
    );
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
   * actions after detect offline mode
   */
  @Override
  public void onOffline() {

    if (isViewAttached()) {
      view.showOffline();
    }
    getForecastAsync();

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
        loadForecast(lastParameters);
      } else {
        view.stopRefreshing();
      }
    }
  }

  @Override
  public void onDatabaseOperationFinished() {
    if(forecastInfoWrapper.size() > 0){
      ForecastInfo lastForecastInfo = forecastInfoWrapper.get(0);
      //if cache hit
      if(lastForecastInfo != null){
        if (isViewAttached()) {
          uiModel = transformModelToUiModel(lastForecastInfo);
          view.showForecast(uiModel);
        }
      }
    }

  }

  //******************** ROOM FUNCTIONS ********************
  private void getForecastAsync() {

    databaseHandler.execute(
        new Runnable() {
          @Override
          public void run() {
            forecastInfoWrapper.clear();

            List<Forecast> forecastList = database.getForecastDAO().getForecast();
            //TODO RESEARCH if only livedata can return null list
            //TODO i.e RESEARCH if this if is necesary
            if (forecastList != null) {

              forecastList = filter.filterByValidUntil(forecastList);
              String query = lastParameters.get(Constant.API_PARAMETER_QUERY);
              if (query != null){
                forecastList = filter.filterByText(forecastList, query);
              } else {
                String latitude = lastParameters.get(Constant.API_PARAMETER_LATITUDE);
                String longitude = lastParameters.get(Constant.API_PARAMETER_LONGITUDE);
                forecastList = filter.filterByLatidudeAndLongitude(forecastList, latitude, longitude);
              }
              Forecast forecast = filter.filterByLastRefresh(forecastList);
              if(forecast != null){

                List<ForecastItemDB> forecastItemList =
                    database.getForecastDAO().getForecastItems(forecast.getId());

                ForecastInfo forecastInfo = transformer.getInfoForecastFromDataBase(forecast, forecastItemList);
                forecastInfoWrapper.add(forecastInfo);
              }
            }
          }
        }
    );
  }


}
