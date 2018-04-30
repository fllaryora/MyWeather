package training.globant.myweather.presentation.show_weather;

/**
 * Contract between view and presenter
 * Created by francisco on 29/04/18.
 */

public interface ShowWeatherContract {
    interface View {

        void showError(String error);
        //actions that now idk
    }
    interface Presenter {

        void attachView(ShowWeatherContract.View view);
        boolean isViewAttached();
        //actions that now idk
    }
}
