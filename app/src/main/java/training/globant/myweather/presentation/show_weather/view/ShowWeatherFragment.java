package training.globant.myweather.presentation.show_weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import training.globant.myweather.R;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;
import training.globant.myweather.presentation.show_weather.presenter.ShowWeatherPresenter;

/**
 * Show the weather of a city
 * Created by francisco on 29/04/18.
 */

public class ShowWeatherFragment extends Fragment implements ShowWeatherContract.View {

  ShowWeatherPresenter presenter;
  private TextView city;
  private TextView maxTemperature;
  private TextView minTemperature;
  private TextView temperature;
  private TextView sky;
  private ImageView refreshImageView;

  /**
   * Used by Activity
   */
  public static ShowWeatherFragment newInstance() {
    return new ShowWeatherFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowWeatherPresenter();
    presenter.attachView(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_show_weather, container, false);
    city = (TextView) view.findViewById(R.id.cityLabel);
    maxTemperature = (TextView) view.findViewById(R.id.maxTemperatureLabel);
    minTemperature = (TextView) view.findViewById(R.id.minTemperatureLabel);
    temperature = (TextView) view.findViewById(R.id.temperatureLabel);
    sky = (TextView) view.findViewById(R.id.skyLabel);
    refreshImageView = (ImageView) view.findViewById(R.id.refreshImageView);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.loadWeather();
  }

  public void searchWeather(String textSubmitted) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, textSubmitted);
    presenter.loadWeather(parameters);
  }

  @Override
  public void showWeather(WeatherUI uiModel) {
    city.setText(uiModel.getCityLabel());
    maxTemperature
        .setText(uiModel.getMaxTemperatureLabel() + " " + getString(R.string.degrees_celsius));
    minTemperature
        .setText(uiModel.getMinTemperatureLabel() + " " + getString(R.string.degrees_celsius));
    temperature.setText(uiModel.getTemperatureLabel() + " " + getString(R.string.degrees_celsius));
    sky.setText(uiModel.getSkyLabel());
    refreshImageView.setImageResource(uiModel.getIcon());
  }

  @Override
  public void showError(String error) {
    View showWeatherView = getView();
    if (showWeatherView != null) {
      Snackbar.make(showWeatherView, "Cannot load the weather", Snackbar.LENGTH_LONG).show();
    }
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      // ReposByUsernameUI reposByUsernameUI = savedInstanceState.getParcelable(KEY_REPOS_BY_USERNAME);
      // presenter.restoreStateAndShowRepos(reposByUsernameUI);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //outState.putParcelable(KEY_REPOS_BY_USERNAME, presenter.getUiModel());
  }
}
