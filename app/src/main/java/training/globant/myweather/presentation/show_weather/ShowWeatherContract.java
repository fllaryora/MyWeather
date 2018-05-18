package training.globant.myweather.presentation.show_weather;

import android.content.Context;
import java.util.Map;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.device.sensors.location.GeoLocationProvider;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * Represents a contract between the view and the presenter.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public interface ShowWeatherContract {

  interface View {

    /**
     * Shows the uiModel element in view.
     *
     * @param uiModel is the view model
     */
    void showWeather(WeatherUI uiModel);

    /**
     * Shows the error in view.
     *
     * @param error is the message/description of an error
     */

    void showError(String error);

    /**
     * Stops the Refreshing action of  SwipeRefreshLayout
     */
    void stopRefreshing();

    /**
     * Returns provider of location
     * @return provider of location
     */
    GeoLocationProvider getGPSGeoLocationProvider();
    //actions that now idk

    /**
     * Returns Invalid Query String Resource
     *
     * @return Invalid Query String Resource
     */
    String getInvalidQueryString();

    /**
     * Returns Api Value Degrees Type String Resource
     *
     * @return Api Value Degrees Type String Resource
     */
    String getApiValueDegreesTypeString();

    /**
     * REturns Api Value Lang String Resource
     *
     * @return Api Value Lang String Resource
     */
    String getApiValueLangString();
    //+string resources
  }

  interface Presenter {

    /**
     * Attaches the view to this presenter.
     *
     * @param view instance
     */
    void attachView(ShowWeatherContract.View view);

    /**
     * Returns true if this presenter has the view attached.
     *
     * @return true if this presenter has the view attached.
     */
    boolean isViewAttached();

    /**
     * Dettaches the view to this presenter.
     */
    void dettachView();

    /**
     * Loads the weather using parameters
     *
     * @param parameters pair key-value data that describe a location
     */
    void loadWeather(Map<String, String> parameters);

    /**
     * Loads the weather using gps
     *
     * @param parameters pair key-value data that describe a location
     */
    void loadWeatherGPS(Map<String, String> parameters);

    /**
     * Transforms the model into a view model instance.
     *
     * @param model data model instance
     * @return view model instance
     */
    WeatherUI transformModelToUiModel(WeatherInfo model);

    /**
     * Return the view model instance.
     * The view onSaveInstanceState will ask the presenter for the current view model.
     *
     * @return view model instance
     */
    WeatherUI getUiModel();

    /**
     * Restores the state and show weather model.
     * The view onActivityCreated will try restore the old view model if it exists.
     *
     * @param uiModel view model
     */
    void restoreStateAndShowWeather(WeatherUI uiModel);

    /**
     * Refresh the weather using lastParameters
     *
     * @param lastParameters pair key-value data that describe a location
     */
    void refreshWeather(Map<String, String> lastParameters);
    //actions that now idk
  }
}
