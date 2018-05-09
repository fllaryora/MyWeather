package training.globant.myweather.presentation.show_weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    setHasOptionsMenu(true);
    presenter = new ShowWeatherPresenter();
    presenter.attachView(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    presenter.dettachView();
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
    Map<String, String> parameters = new HashMap<String, String>();
    //TODO HARDCODED PARAMS - USE GPS IN FUTURE
    parameters.put(Constant.API_PARAMETER_QUERY, "Córdoba,AR");
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

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_main, menu);
    final MenuItem mSearchItem = (MenuItem) menu.findItem(R.id.m_search);

    //https://stackoverflow.com/questions/45335853/cant-set-onactionexpandlistener-this-is-not-supported-use-menuitemcompat-seto/45431091
    final SearchView searchView = (SearchView) mSearchItem.getActionView();
    //TODO ADD ADAPTER TO ADD SUGGESTIONS
    //searchView.setSuggestionsAdapter(mAdapter);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String textSubmitted) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constant.API_PARAMETER_QUERY, textSubmitted);
        presenter.loadWeather(parameters);
        //true if the query has been handled by the listener,
        // false to let the SearchView perform the default action.
        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        //false if the SearchView should perform the default action of showing any suggestions if available,
        // true if the action was handled by the listener.
        return true;
      }
    });

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        // true if the listener wants to override the default behavior
        // of clearing the text field and dismissing it, false otherwise.
        return false;
      }
    });

  }

}
