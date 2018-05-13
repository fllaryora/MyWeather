package training.globant.myweather.presentation.show_weather.view;

import android.Manifest;
import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import training.globant.myweather.device.utils.DeviceConstant;
import training.globant.myweather.device.utils.DeviceConstant.RequestCodes;
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
  private TextView maxLabel;
  private TextView minLabel;
  private TextView temperature;
  private TextView sky;
  private ImageView refreshImageView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private Map<String, String> lastQuery;
  private ProgressDialog progressDialog;

  public static ShowWeatherFragment newInstance() {
    return new ShowWeatherFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowWeatherPresenter();
    presenter.attachView(this);
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please wait.");
    progressDialog.setCancelable(false);

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
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    hintLabel = (TextView) view.findViewById(R.id.hintLabel);
    city = (TextView) view.findViewById(R.id.cityLabel);
    maxTemperature = (TextView) view.findViewById(R.id.maxTemperatureLabel);
    minTemperature = (TextView) view.findViewById(R.id.minTemperatureLabel);
    maxLabel = (TextView) view.findViewById(R.id.maxLabel);
    minLabel = (TextView) view.findViewById(R.id.minLabel);
    temperature = (TextView) view.findViewById(R.id.temperatureLabel);
    sky = (TextView) view.findViewById(R.id.skyLabel);
    refreshImageView = (ImageView) view.findViewById(R.id.refreshImageView);

    setHasOptionsMenu(true);
    setUpSwipeToRefresh();

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    tryLocation();
  }

  /**
   * Shows uiModel Weather in the view
   *
   * @param uiModel is the view model
   */
  @Override
  public void showWeather(WeatherUI uiModel) {
    //TODO for the love of god bring to another fragment the message when I have time
    hintLabel.setVisibility(View.GONE);
    maxLabel.setVisibility(View.VISIBLE);
    minLabel.setVisibility(View.VISIBLE);

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
    stopRefreshing(false);
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
      progressDialog.dismiss();
      stopRefreshing(true);
      Snackbar.make(showWeatherView, R.string.can_not_load_message, Snackbar.LENGTH_LONG).show();
    }
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
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(Constant.KEY_WEATHER, presenter.getUiModel());
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
   *
   * @param showEmpty background
   */
  @Override
  public void stopRefreshing(boolean showEmpty) {
    swipeRefreshLayout.setRefreshing(false);
    if (showEmpty) {
      //showEmptyUsernameError();
      //TODO for the love of god bring to another fragment the message when I have time
      hintLabel.setVisibility(View.VISIBLE);
      maxLabel.setVisibility(View.GONE);
      minLabel.setVisibility(View.GONE);
    }
  }

  /*************** GPS *********************/
  public void tryLocation() {
    if (hasSystemGPS()) {
      if(isProviderEnabled()){
        if (validatePermission()) {
          if (shouldWeShowAnExplanation()) {
            Toast.makeText(getActivity(), R.string.explanation, Toast.LENGTH_SHORT).show();
          }
          requestPermissionsToUser();
          //The callback method (onRequestPermissionsResult) gets the result of the request.
          return;
        }
        progressDialog.show();
        presenter.loadWeather(getActivity());
      } else {
        displayPromptForEnablingGPS();
      }

    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == RequestCodes.REQUEST_GPS.getRequestCode()) {
      boolean permissionGranted = true;

      for(int i = 0; i < permissions.length ; i++){
        if(DeviceConstant.isAccessInAccessLocation(permissions[i])
            && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            permissionGranted = false;
            break;
          }
      }

      if(permissionGranted){
        progressDialog.show();
        presenter.loadWeather(getActivity());
      } else {
        Toast.makeText(getActivity(), R.string.permission_wont_granted, Toast.LENGTH_SHORT).show();
        //show empty
        //TODO for the love of god bring to another fragment the message when I have time
        hintLabel.setVisibility(View.VISIBLE);
        maxLabel.setVisibility(View.GONE);
        minLabel.setVisibility(View.GONE);
      }
    } else {
      super.onRequestPermissionsResult( requestCode, permissions, grantResults);
    }
  }

  private boolean validatePermission() {
    return ActivityCompat
        .checkSelfPermission(getActivity(), permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat
        .checkSelfPermission(getActivity(), permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED;
  }

  private boolean shouldWeShowAnExplanation() {
    return shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_COARSE_LOCATION) &&
        shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_FINE_LOCATION);
  }

  private void requestPermissionsToUser() {
    requestPermissions(DeviceConstant.accessLocations,
            RequestCodes.REQUEST_GPS.getRequestCode()
        );
  }

  private boolean isProviderEnabled() {
    LocationManager locationManager = (LocationManager) getActivity()
        .getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  private boolean hasSystemGPS() {
    return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
  }

  private void displayPromptForEnablingGPS() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
    final String message = getString(R.string.enable_gps_message);

    builder.setMessage(message)
        .setPositiveButton(getString(R.string.ok),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface d, int id) {
                getActivity().startActivity(new Intent(action));
                d.dismiss();
              }
            });

    builder.create().show();
  }

  /*************** GPS *********************/

}
