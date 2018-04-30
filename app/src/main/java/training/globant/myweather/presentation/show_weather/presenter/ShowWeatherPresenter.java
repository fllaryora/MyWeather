package training.globant.myweather.presentation.show_weather.presenter;

import training.globant.myweather.presentation.show_weather.ShowWeatherContract;

/**
 * Presenter that handles user actions from {@link training.globant.myweather.presentation.show_weather.view.ShowWeatherFragment} view,
 * show....
 * <p>
 * Notice that Presenter knows nothing about Android framework.
 * Created by francisco on 29/04/18.
 */

public class ShowWeatherPresenter implements ShowWeatherContract.Presenter {

    private ShowWeatherContract.View view;

    @Override
    public void attachView(ShowWeatherContract.View view) {
        this.view = view;
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }
}
