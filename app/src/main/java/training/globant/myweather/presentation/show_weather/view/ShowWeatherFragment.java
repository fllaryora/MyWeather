package training.globant.myweather.presentation.show_weather.view;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
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
  private ProgressDialog progressDialog;
  private PermissionsHelper permissionsHelper;
  private PermissionHelperCallback helperCallback;
  private WeatherUI uiModel;
  private boolean isVisible;

  public static ShowWeatherFragment newInstance() {
    return new ShowWeatherFragment();
  }

  /***************** ANDROID LIFE-CYCLE FUNCTIONS ************************/

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowWeatherPresenter();
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
      uiModel = savedInstanceState.getParcelable(Constant.KEY_WEATHER);
    }
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      uiModel = savedInstanceState.getParcelable(Constant.KEY_WEATHER);
    }
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    isVisible = isVisibleToUser;
    // Make sure that fragment is currently visible
    //avoid null pointer when is called too early in the life cycle
    if (isVisible && isResumed()) {
      loadScreen();
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    loadScreen();
  }

  @Override
  public void onLocationChange() {
    //avoid null pointer when is called too early in the life cycle
    if (isResumed()) {
      loadScreen();
    }
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
    permissionsHelper
        .onRequestPermissionsResult(requestCode, permissions, grantResults, helperCallback);

  }

  /***************** HELPER LIFE-CYCLE FUNCTIONS ************************/
  private void loadScreen(){
    if (!getUserVisibleHint()) {
      return;
    }
    presenter.attachView(this);
    presenter.restoreStateAndShowWeather(uiModel, isModelValid());
    if (presenter.getUiModel() == null) {
      permissionsHelper.tryLocation(helperCallback);
    }
  }
  private void setModelAsValid(){
    Editor editor = getActivity().getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE).edit();
    editor.putBoolean(Constant.KEY_WEATHER_MODEL_VALID, Constant.VALID);
    editor.commit();
  }

  private Map<String, String> getLastQuery(){
    SharedPreferences sharedPref = getActivity()
        .getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE);
    Map<String, String> lastQuery = new HashMap<String, String>();
    lastQuery.put(Constant.API_PARAMETER_QUERY,
        sharedPref.getString(Constant.API_PARAMETER_QUERY, null));
    return lastQuery;
  }

  public boolean isModelValid(){
    SharedPreferences sharedPref = getActivity()
        .getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE);
    return sharedPref.getBoolean(Constant.KEY_WEATHER_MODEL_VALID, Constant.INVALID);
  }

  private void createHelperPermissionCallBack() {
    helperCallback = new PermissionHelperCallback() {
      @Override
      public void onRequestPermissionsResultFail(int requestCode,
          @NonNull String[] permissions, @NonNull int[] grantResults) {
        ShowWeatherFragment.super
            .onRequestPermissionsResult(requestCode, permissions, grantResults);
      }

      @Override
      public void onResponse() {
        progressDialog.show();
        presenter.loadWeather(getLastQuery());
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
    progressDialog.setTitle(getString(R.string.loading));
    progressDialog.setMessage(getString(R.string.please_wait));
    progressDialog.setCancelable(false);
  }

  private void setUpSwipeToRefresh() {
    swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            presenter.refreshWeather(getLastQuery());
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

    city.setText(String.format(getString(R.string.city_country_format), uiModel.getCityLabel(),
        uiModel.getCountryLabel()));
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

    setModelAsValid();
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
    Snackbar.make(getView(), (error != null) ? error : getString(R.string.can_not_load_message),
        Snackbar.LENGTH_LONG).show();
    setModelAsValid();
    progressDialog.dismiss();
    stopRefreshing();
  }

  /**
   * Returns PermissionsHelper
   *
   * @return PermissionsHelper
   */
  @Override
  public PermissionsHelper getPermissionHelper() {
    return permissionsHelper;
  }

  /********************* String Resources *********************/
  @Override
  public String getInvalidQueryString() {
    return getString(R.string.invalid_query);
  }

}
