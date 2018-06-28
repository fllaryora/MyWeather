package training.globant.myweather.presentation.show_weather.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.database.AppDatabase;
import training.globant.myweather.data.database.DatabaseHandler;
import training.globant.myweather.data.database.entities.Weather;
import training.globant.myweather.data.database.filters.WeatherFilter;
import training.globant.myweather.data.database.transformer.WeatherTransformer;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.TemperatureFormatter;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * {@link ShowWeatherContract.Presenter} that controls communication between views and view models
 * of the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowWeatherPresenter implements ShowWeatherContract.Presenter, WeatherCallback,
    DatabaseHandler.Callback {

  private final List<WeatherInfo> weatherInfoWrapper;
  private ShowWeatherContract.View view;
  private WeatherUI uiModel;
  private SearchWeatherInteractor searchWeatherInteractor;
  private DatabaseHandler databaseHandler;
  private AppDatabase database;
  private Map<String, String> lastParameters;
  private WeatherTransformer transformer;
  private WeatherFilter filter;

  public ShowWeatherPresenter(AppDatabase database, SearchWeatherInteractor searchWeatherInteractor) {
    this.database = database;
    this.searchWeatherInteractor = searchWeatherInteractor;
    weatherInfoWrapper = new ArrayList<WeatherInfo>();
    databaseHandler = new DatabaseHandler(database, this);
    transformer = new WeatherTransformer();
    filter = new WeatherFilter();
  }

  /**
   * Holds ShowWeatherContract.View view in a member
   *
   * @param view instance
   */
  @Override
  public void attachView(ShowWeatherContract.View view) {
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
   * Load the weather using parameters
   *
   * @param parameters pair key-value data that describe a location
   */
  @Override
  public void loadWeather(Map<String, String> parameters) {
    if (!hasParametersAQuery(parameters)) {
      searchWeatherInteractor.executeGPS(view.getPermissionHelper(), this);
    } else {
      String query = parameters.get(Constant.API_PARAMETER_QUERY);
      if (isQueryValid(query)) {
        doRequest(parameters);
      } else {
        if (isViewAttached()) {
          view.showError(view.getInvalidQueryString());
        }
      }
    }
  }

  /**
   * Called when parameters are ready to be localized by api
   */
  private void doRequest(final Map<String, String> parameters) {
    lastParameters = parameters;
    searchWeatherInteractor.execute(lastParameters, this);
  }

  /**
   * actions taken After being geo-localized
   *
   * @param parameters with coordinates
   */
  @Override
  public void onGeolocation(final Map<String, String> parameters) {
    doRequest(parameters);
  }

  private boolean hasParametersAQuery(Map<String, String> parameters) {
    lastParameters = parameters;
    if (parameters == null) {
      return false;
    }
    String query = parameters.get(Constant.API_PARAMETER_QUERY);
    return query != null;
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
  public WeatherUI transformModelToUiModel(WeatherInfo model) {
    if (model == null) {
      return null;
    }
    String cityName = model.getName();
    String countryName = model.getCountry().getName();
    String description = model.getSkyDescription().get(0).getDescription();
    String temperatureInfo = TemperatureFormatter
        .getTemperatureFormatted(model.getTemperatureInfo().getTemperature());
    String maxTemperatureInfo = TemperatureFormatter
        .getTemperatureFormatted(model.getTemperatureInfo().getMaximum());
    String minTemperatureInfo = TemperatureFormatter
        .getTemperatureFormatted(model.getTemperatureInfo().getMinimum());
    int icon = IconMapper.fromInt(model.getSkyDescription().get(0).getId()).getIcon();

    return new WeatherUI(cityName, maxTemperatureInfo, minTemperatureInfo, temperatureInfo,
        description, icon, countryName);
  }

  /**
   * Returns the view model
   *
   * @return view model instance
   */
  @Override
  public WeatherUI getUiModel() {
    return uiModel;
  }

  /**
   * Restores the state and show weather model.
   * Shows the view model if it exists.
   *
   * @param uiModel view model
   */
  @Override
  public void restoreStateAndShowWeather(WeatherUI uiModel, boolean isValid) {
    if (!isValid) {
      uiModel = null;
      this.uiModel = null;
    }
    if (isViewAttached()) {
      if (uiModel != null) {
        this.uiModel = uiModel;
        view.showWeather(uiModel);
      }
    }
  }

  /**
   * Connect the data model to the view after a successful response
   *
   * @param weatherInfo data model of the weather
   */
  @Override
  public void onResponse(final WeatherInfo weatherInfo) {
    weatherInfoWrapper.clear();
    weatherInfoWrapper.add(weatherInfo);
    databaseHandler.execute(
        new Runnable() {
          @Override
          public void run() {
            Weather weatherEntity = transformer
                .getDataBaseWeatherFromInfo(lastParameters, weatherInfo);
            database.getWeatherDAO().insert(weatherEntity);
          }
        }
    );
  }


  @Override
  public void onResponse(ForecastInfo forecastInfo) {
    //never used
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
    getWeatherAsync();
  }

  /**
   * Refresh the weather loading the weather again using lastParameters
   *
   * @param lastParameters pair key-value data that describe a location
   */
  @Override
  public void refreshWeather(Map<String, String> lastParameters) {
    if (isViewAttached()) {
      if (lastParameters != null && !lastParameters.isEmpty()) {
        loadWeather(lastParameters);
      } else {
        view.stopRefreshing();
      }
    }
  }


  @Override
  public void onDatabaseOperationFinished() {

    if (weatherInfoWrapper.size() > 0) {
      WeatherInfo lastWeatherInfo = weatherInfoWrapper.get(0);
      //if cache hit
      if (lastWeatherInfo != null) {
        if (isViewAttached()) {
          uiModel = transformModelToUiModel(lastWeatherInfo);
          view.showWeather(uiModel);
        }
      }
    }

  }

  //******************** ROOM FUNCTIONS ********************

  private void getWeatherAsync() {

    databaseHandler.execute(
        new Runnable() {
          @Override
          public void run() {
            weatherInfoWrapper.clear();
            List<Weather> weatherList = database.getWeatherDAO().getWeathers();
            //TODO RESEARCH if only livedata can return null list
            //TODO i.e RESEARCH if this if is necesary
            if (weatherList != null) {
              weatherList = filter.filterByValidUntil(weatherList);
              String query = lastParameters.get(Constant.API_PARAMETER_QUERY);
              if (query != null) {
                weatherList = filter.filterByText(weatherList, query);
              } else {
                String latitude = lastParameters.get(Constant.API_PARAMETER_LATITUDE);
                String longitude = lastParameters.get(Constant.API_PARAMETER_LONGITUDE);
                weatherList = filter.filterByLatidudeAndLongitude(weatherList, latitude, longitude);
              }
              Weather weather = filter.filterByLastRefresh(weatherList);
              if (weather != null) {
                WeatherInfo weatherInfo = transformer.getInfoWeatherFromDataBase(weather);
                weatherInfoWrapper.add(weatherInfo);
              }
            }
          }
        }
    );
  }

}
