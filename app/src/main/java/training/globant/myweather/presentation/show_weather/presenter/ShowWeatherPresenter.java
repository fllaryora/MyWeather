package training.globant.myweather.presentation.show_weather.presenter;

import static training.globant.myweather.data.utils.Constant.DECIMAL_FORMAT_PATTERN;
import static training.globant.myweather.data.utils.Constant.DECIMAL_SEPARATOR;

import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * Presenter that handles user actions from {@link training.globant.myweather.presentation.show_weather.view.ShowWeatherFragment}
 * view, show.... <p> Notice that Presenter knows nothing about Android framework. Created by
 * francisco on 29/04/18.
 */

public class ShowWeatherPresenter implements ShowWeatherContract.Presenter, WeatherCallback {

  private ShowWeatherContract.View view;
  private WeatherUI uiModel;

  @Override
  public void attachView(ShowWeatherContract.View view) {
    this.view = view;
  }

  @Override
  public boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void dettachView() {
    this.view = null;
  }

  @Override
  public void loadWeather(Map<String, String> parameters) {
    SearchWeatherInteractor searchWeatherInteractor = new SearchWeatherInteractor();
    String query = parameters.get(Constant.API_PARAMETER_QUERY);
    if (isQueryValid(query)) {
      searchWeatherInteractor.execute(parameters, this);
    } else {
      if (isViewAttached()) {
        view.showError("Invalid Query");
      }
    }
  }

  private boolean isQueryValid(String query) {
    if (query == null) {
      return false;
    }
    query = query.trim();
    if (TextUtils.isEmpty(query)) {
      return false;
    }
    return query.length() > 4;
  }

  @Override
  public WeatherUI transformModelToUiModel(WeatherInfo model) {
    //TODO change DEScription
    //https://openweathermap.org/weather-conditions
    String cityName = "";
    String description = "";
    String temperatureInfo = "";
    String maxTemperatureInfo = "";
    String minTemperatureInfo = "";
    int icon = 0;

    if (model.getSkyDescription() != null && model.getSkyDescription().size() > 0) {
      description = model.getSkyDescription().get(0).getDescription();
      icon = IconMapper.fromInt(model.getSkyDescription().get(0).getId()).getIcon();
    }

    if (model.getTemperatureInfo() != null) {
      //https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
      DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
      symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
      DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT_PATTERN, symbols);
      temperatureInfo = formatter.format(model.getTemperatureInfo().getTemperature());
      maxTemperatureInfo = formatter.format(model.getTemperatureInfo().getMaximalTemperature());
      minTemperatureInfo = formatter.format(model.getTemperatureInfo().getMinimalTemperature());
    }

    if (model.getName() != null) {
      cityName = model.getName();
    }

    return new WeatherUI(cityName, maxTemperatureInfo, minTemperatureInfo, temperatureInfo,
        description, icon);
  }

  @Override
  public WeatherUI getUiModel() {
    return uiModel;
  }

  @Override
  public void onResponse(WeatherInfo weatherInfo) {
    if (isViewAttached()) {
      uiModel = transformModelToUiModel(weatherInfo);
      view.showWeather(uiModel);
    }
  }

  @Override
  public void onError(String error) {
    if (isViewAttached()) {
      view.showError(error);
    }
  }
}
