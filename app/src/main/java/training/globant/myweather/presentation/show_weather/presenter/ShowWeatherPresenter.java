package training.globant.myweather.presentation.show_weather.presenter;

import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
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

  public void loadWeather() {
    Map<String, String> parameters = new HashMap<String, String>();
    //TODO HARDCODED PARAMS - USE GPS IN FUTURE
    parameters.put(Constant.API_PARAMETER_QUERY, "Córdoba,AR");
    SearchWeatherInteractor searchWeatherInteractor = new SearchWeatherInteractor();
    searchWeatherInteractor.execute(parameters, this);
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
    //TODO change DEScription and add icons
    //https://openweathermap.org/weather-conditions
    String cityName = "";
    String description = "";
    String temperatureInfo = "";

    if (model.getMetaInfo() != null && model.getMetaInfo().size() > 0) {
      description = model.getMetaInfo().get(0).getDescription();
    }

    if (model.getTemperatureInfo() != null) {
      //https://stackoverflow.com/questions/14389349/android-get-current-locale-not-default
      DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
      symbols.setDecimalSeparator(',');
      DecimalFormat formatter = new DecimalFormat("##.#", symbols);
      temperatureInfo = formatter.format(model.getTemperatureInfo().getTemperature());
    }

    if (model.getName() != null) {
      cityName = model.getName();
    }

    return new WeatherUI(cityName, temperatureInfo, description);
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
