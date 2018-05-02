package training.globant.myweather.presentation.show_weather.view;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;

/**
 * Show the weather of a city
 * Created by francisco on 29/04/18.
 */

public class ShowWeatherFragment extends Fragment implements ShowWeatherContract.View {
    /**
     * Used by Activity
     * @return
     */
    public static ShowWeatherFragment newInstance(){
        return new ShowWeatherFragment();
    }

    @Override
    public void showError(String error) {
        View showWeatherView = getView();
        if( showWeatherView != null ){
            Snackbar.make(showWeatherView, "Cannot load the weather", Snackbar.LENGTH_LONG).show();
        }
    }
}
