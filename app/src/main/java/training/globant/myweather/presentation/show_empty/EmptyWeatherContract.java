package training.globant.myweather.presentation.show_empty;

import java.util.Map;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * Created by francisco on 19/05/18.
 */

public interface EmptyWeatherContract {

  interface View {

    /**
     * Shows the uiModel element in view.
     *
     * @param uiModel is the view model
     */
    void navigateToShowWeather(WeatherUI uiModel);

    /**
     * Shows the error in view.
     *
     * @param error is the message/description of an error
     */

    void showError(String error);

    /**
     * Returns PermissionsHelper
     * @return PermissionsHelper
     */
    PermissionsHelper getPermissionHelper();
    //actions that now idk

    /**
     * Returns Invalid Query String Resource
     *
     * @return Invalid Query String Resource
     */
    String getInvalidQueryString();

    //+string resources
  }

  interface Presenter {

    /**
     * Attaches the view to this presenter.
     *
     * @param view instance
     */
    void attachView(EmptyWeatherContract.View view);

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
     * Goes to weather using parameters
     *
     * @param parameters pair key-value data that describe a location
     */
    void goToWeather(Map<String, String> parameters);

    /**
     * Transforms the model into a view model instance.
     *
     * @param model data model instance
     * @return view model instance
     */
    WeatherUI transformModelToUiModel(WeatherInfo model);

    //actions that now idk
  }

}
