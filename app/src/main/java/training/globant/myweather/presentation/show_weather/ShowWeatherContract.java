package training.globant.myweather.presentation.show_weather;

import java.util.Map;

import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * Contract between view and presenter
 * Created by francisco on 29/04/18.
 */

public interface ShowWeatherContract {
    interface View {
        void showWeather(WeatherUI uiModel);
        void showError(String error);
        //actions that now idk
    }
    interface Presenter {

        void attachView(ShowWeatherContract.View view);
        boolean isViewAttached();

        void loadWeather(Map<String,String> parameters);
        WeatherUI transformModelToUiModel(WeatherInfo model);
        WeatherUI getUiModel();
        //actions that now idk
    }
}
