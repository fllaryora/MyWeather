package training.globant.myweather.presentation.show_empty.presenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_empty.EmptyWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * {@link EmptyWeatherContract.Presenter} that controls communication between views and view models
 * of the presentation layer.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class ShowEmptyPresenter implements EmptyWeatherContract.Presenter, WeatherCallback {

  private EmptyWeatherContract.View view;
  private WeatherUI uiModel;

  /**
   * Holds ShowWeatherContract.View view in a member
   *
   * @param view instance
   */
  @Override
  public void attachView(EmptyWeatherContract.View view) {
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
   * Connect the data model to the view after a successful response
   *
   * @param weatherInfo data model of the weather
   */
  @Override
  public void onResponse(WeatherInfo weatherInfo) {
    if (isViewAttached()) {
      uiModel = transformModelToUiModel(weatherInfo);
      view.navigateToShowWeather(uiModel);
    }
  }

  @Override
  public void onResponse(ForecastInfo weatherInfo) {

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
   * Gets the weather using parameters to go to weather view
   *
   * @param parameters pair key-value data that describe a location
   */
  @Override
  public void goToWeather(Map<String, String> parameters) {
    SearchWeatherInteractor searchWeatherInteractor = new SearchWeatherInteractor();
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
  public WeatherUI transformModelToUiModel(WeatherInfo model) {
    if (model == null) {
      return null;
    }

    String cityName = model.getName();
    String description = model.getSkyDescription().get(0).getDescription();
    String temperatureInfo = getTemperatueFormated(model.getTemperatureInfo().getTemperature());
    String maxTemperatureInfo = getTemperatueFormated(model.getTemperatureInfo().getMaximum());
    String minTemperatureInfo = getTemperatueFormated(model.getTemperatureInfo().getMinimum());
    int icon = IconMapper.fromInt(model.getSkyDescription().get(0).getId()).getIcon();

    return new WeatherUI(cityName, maxTemperatureInfo, minTemperatureInfo, temperatureInfo,
        description, icon);
  }

  private String getTemperatueFormated(double temperature) {
    //https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setDecimalSeparator(Constant.DECIMAL_SEPARATOR);
    DecimalFormat formatter = new DecimalFormat(Constant.DECIMAL_FORMAT_PATTERN, symbols);
    return formatter.format(temperature);
  }

}
