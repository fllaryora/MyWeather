package training.globant.myweather.presentation.show_weather.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import training.globant.myweather.R;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.PermissionHelperCallback;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;
import training.globant.myweather.presentation.show_weather.presenter.ShowWeatherPresenter;

/**
 * Represents a View in a model view presenter (MVP) pattern.
 * In this case is used as a view of {@link WeatherUI}.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowWeatherFragment extends Fragment implements ShowWeatherContract.View {

  private ShowWeatherPresenter presenter;
  private TextView hintLabel;
  private TextView city;
  private TextView maxTemperature;
  private TextView minTemperature;
  private TextView temperature;
  private TextView sky;
  private ImageView refreshImageView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private Map<String, String> lastQuery;
  private ProgressDialog progressDialog;
  private PermissionsHelper permissionsHelper;
  private PermissionHelperCallback helperCallback;

  public static ShowWeatherFragment newInstance() {
    return new ShowWeatherFragment();
  }

  /***************** ANDROID LIFE-CYCLE FUNCTIONS ************************/

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowWeatherPresenter();
    presenter.attachView(this);
    permissionsHelper = new PermissionsHelper(this);
    progressDialogSetup();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_show_weather, container, false);
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    hintLabel = (TextView) view.findViewById(R.id.text_empty);
    city = (TextView) view.findViewById(R.id.cityLabel);
    maxTemperature = (TextView) view.findViewById(R.id.maxTemperatureLabel);
    minTemperature = (TextView) view.findViewById(R.id.minTemperatureLabel);
    temperature = (TextView) view.findViewById(R.id.temperatureLabel);
    sky = (TextView) view.findViewById(R.id.skyLabel);
    refreshImageView = (ImageView) view.findViewById(R.id.refreshImageView);
    createHelperPermissionCallBack();
    setHasOptionsMenu(true);
    setUpSwipeToRefresh();
    if (savedInstanceState != null) {
      WeatherUI uiModel = savedInstanceState.getParcelable(Constant.KEY_WEATHER);
      presenter.restoreStateAndShowWeather(uiModel);
    }
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      WeatherUI uiModel = savedInstanceState.getParcelable(Constant.KEY_WEATHER);
      presenter.restoreStateAndShowWeather(uiModel);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.attachView(this);
    if (presenter.getUiModel() == null) {
      permissionsHelper.tryLocation(helperCallback);
    }
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
        progressDialog.show();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constant.API_PARAMETER_QUERY, textSubmitted);
        lastQuery = parameters;
        presenter.loadWeather(parameters);
        mSearchItem.collapseActionView();
        //true when the query has been handled by the listener,
        // false to let the SearchView perform the default action.
        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        //false when the SearchView should perform the default action of showing any suggestions when is available,
        // true when the action was handled by the listener.
        return true;
      }
    });

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        // true when the listener wants to override the default behavior
        // of clearing the text field and dismissing it, false otherwise.
        return false;
      }
    });

  }

  @Override
  public void onPause() {
    super.onPause();
    stopRefreshing();
    presenter.dettachView();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(Constant.KEY_WEATHER, presenter.getUiModel());
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults) {
    /*
     * Sometimes is called after onPause and before onResult
     */
    presenter.attachView(this);
    permissionsHelper.onRequestPermissionsResult(requestCode,permissions,grantResults, helperCallback);

  }

  /***************** HELPER LIFE-CYCLE FUNCTIONS ************************/

  private void createHelperPermissionCallBack(){
    helperCallback = new PermissionHelperCallback() {
      @Override
      public void onRequestPermissionsResultFail(int requestCode,
          @NonNull String[] permissions, @NonNull int[] grantResults) {
        ShowWeatherFragment.super.onRequestPermissionsResult( requestCode, permissions, grantResults);
      }

      @Override
      public void onResponse() {
        progressDialog.show();
        presenter.loadWeather(null);
      }

      @Override
      public void onPermissionDenied() {
        Toast.makeText(getContext(), R.string.permission_wont_granted, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onHasntSystemFeature() {
        Toast.makeText(getContext(), R.string.hasnt_system_feature, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onShowAnExplanation() {
        Toast.makeText(getContext(), R.string.explanation, Toast.LENGTH_SHORT).show();
      }
    };
  }

  private void progressDialogSetup() {
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please wait.");
    progressDialog.setCancelable(false);
  }

  private void setUpSwipeToRefresh() {
    swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            presenter.refreshWeather(lastQuery);
          }
        }
    );
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
  }

  /**
   * Stops the Refreshing action of  SwipeRefreshLayout
   */
  @Override
  public void stopRefreshing() {
    swipeRefreshLayout.setRefreshing(false);
  }

  //***************** VIEW FUNCTIONS ************************/
  /**
   * Shows uiModel Weather in the view
   *
   * @param uiModel is the view model
   */
  @Override
  public void showWeather(WeatherUI uiModel) {
    //TODO for the love of god bring to another fragment the message when I have time
    hintLabel.setVisibility(View.GONE);
    swipeRefreshLayout.setVisibility(View.VISIBLE);

    city.setText(uiModel.getCityLabel());
    String maxTemp = String
        .format(getString(R.string.temperature_template), uiModel.getMaxTemperatureLabel());
    String minTemp = String
        .format(getString(R.string.temperature_template), uiModel.getMinTemperatureLabel());
    String currTemp = String
        .format(getString(R.string.temperature_template), uiModel.getTemperatureLabel());
    maxTemperature.setText(maxTemp);
    minTemperature.setText(minTemp);
    temperature.setText(currTemp);
    sky.setText(uiModel.getSkyLabel());
    refreshImageView.setImageResource(uiModel.getIcon());

    progressDialog.dismiss();
    stopRefreshing();
  }

  /**
   * Shows error in the view
   *
   * @param error is the message/description of an error
   */
  @Override
  public void showError(String error) {
    View showWeatherView = getView();
    if (showWeatherView != null) {
      Snackbar.make(showWeatherView, (error != null)?error: getString(R.string.can_not_load_message) , Snackbar.LENGTH_LONG).show();
    }
    progressDialog.dismiss();
    stopRefreshing();
  }

  /**
   * Returns PermissionsHelper
   * @return PermissionsHelper
   */
  @Override
  public PermissionsHelper getPermissionHelper() {
    return permissionsHelper;
  }

  /********************* String Resources *********************/
  @Override
  public String getInvalidQueryString(){
    return getString(R.string.invalid_query);
  }

}
