package training.globant.myweather.presentation.show_weather.presenter;

import android.content.Context;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * {@link ShowWeatherContract.Presenter} that controls communication between views and view models
 * of the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowWeatherPresenter implements ShowWeatherContract.Presenter, WeatherCallback {

  private ShowWeatherContract.View view;
  private WeatherUI uiModel;
  private SearchWeatherInteractor searchWeatherInteractor;

  public ShowWeatherPresenter(Context context){
    searchWeatherInteractor = new SearchWeatherInteractor(context);
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
        searchWeatherInteractor.execute(parameters, this);
      } else {
        if (isViewAttached()) {
          view.showError(view.getInvalidQueryString());
        }
      }
    }
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
  public WeatherUI transformModelToUiModel(WeatherInfo model) {
    if (model == null) {
      return null;
    }
    String cityName = model.getName();
    String countryName = model.getCountry().getName();
    String description = model.getSkyDescription().get(0).getDescription();
    String temperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getTemperature());
    String maxTemperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getMaximum());
    String minTemperatureInfo = getTemperatureFormatted(model.getTemperatureInfo().getMinimum());
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
    if(!isValid){
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

  private String getTemperatureFormatted(double temperature) {
    //https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setDecimalSeparator(Constant.DECIMAL_SEPARATOR);
    DecimalFormat formatter = new DecimalFormat(Constant.DECIMAL_FORMAT_PATTERN, symbols);
    return formatter.format(temperature);
  }

  /**
   * Connect the data model to the view after a successful response
   *
   * @param weatherInfo data model of the weather
   */
  @Override
  public void onResponse(WeatherInfo weatherInfo) {
    if (isViewAttached()) {
      uiModel = transformModelToUiModel(weatherInfo);
      view.showWeather(uiModel);
    }
  }

  @Override
  public void onResponse(ForecastInfo forecastInfo) {

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
  public void refreshWeather(Map<String, String> lastParameters) {
    if (isViewAttached()) {
      if (lastParameters != null && !lastParameters.isEmpty()) {
        //TODO ADD CONTROL CACHE TO BE SURE THAT WE HAVE A NETWORK RESPONSE
        loadWeather(lastParameters);
      } else {
        view.stopRefreshing();
      }
    }
  }


}
