package training.globant.myweather.presentation.show_empty.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.HashMap;
import java.util.Map;
import training.globant.myweather.R;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.PermissionHelperCallback;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.presentation.show_empty.EmptyWeatherContract;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;
import training.globant.myweather.presentation.show_empty.presenter.ShowEmptyPresenter;


/**
 * Represents a Empty View.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowEmptyFragment extends Fragment implements EmptyWeatherContract.View{
  private ShowEmptyPresenter presenter;
  private PermissionsHelper permissionsHelper;
  private ProgressDialog progressDialog;

  public static ShowEmptyFragment newInstance() {
    return new ShowEmptyFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowEmptyPresenter();
    presenter.attachView(this);
    permissionsHelper = new PermissionsHelper(this);
    progressDialogSetup();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.empty_weather_view, container, false);
    setHasOptionsMenu(true);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    try {
      //TODO start ACTIVITY SHOW WEATER ACTIVITY
    //  mCallback = (ShowWeatherInterface) getActivity();
    } catch (ClassCastException e) {
      throw new ClassCastException(
          String.format(getString(R.string.must_implement_interface), getActivity().toString()));
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.attachView(this);
    PermissionHelperCallback permission = new PermissionHelperCallback() {
      @Override
      public void onResponse() {
        presenter.goToWeather(null);
      }

      @Override
      public void onError() {

      }
    };
    permissionsHelper.tryLocation(permission);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_main, menu);
    final MenuItem mSearchItem = (MenuItem) menu.findItem(R.id.m_search);
    final SearchView searchView = (SearchView) mSearchItem.getActionView();

    //TODO ADD ADAPTER TO ADD SUGGESTIONS
    //searchView.setSuggestionsAdapter(mAdapter);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String textSubmitted) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constant.API_PARAMETER_QUERY, textSubmitted);
        presenter.goToWeather(parameters);
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

  @Override
  public void onPause() {
    super.onPause();
    presenter.dettachView();
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults) {
    /*
     * Sometimes is called after onPause and before onResult
     */
    presenter.attachView(this);
    PermissionHelperCallback permission = new PermissionHelperCallback() {
      @Override
      public void onResponse() {
        presenter.goToWeather(null);
      }

      @Override
      public void onError() {
        ShowEmptyFragment.super.onRequestPermissionsResult( requestCode, permissions, grantResults);
      }
    };
    permissionsHelper.onRequestPermissionsResult(requestCode,permissions,grantResults,permission);

  }

  /***************** HELPER LIFE-CYCLE FUNCTIONS ************************/

  private void progressDialogSetup() {
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please wait.");
    progressDialog.setCancelable(false);
  }


  /***************** VIEW FUNCTIONS ************************/

  @Override
  public void navigateToShowWeather(WeatherUI uiModel) {
    //TODO start ACTIVITY SHOW WEATER ACTIVITY
   // mCallback.callShowWeather(uiModel);
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
      if(error != null){
        Snackbar.make(showWeatherView, R.string.can_not_load_message, Snackbar.LENGTH_LONG).show();
      } else {
        Snackbar.make(showWeatherView, R.string.can_not_load_message, Snackbar.LENGTH_LONG).show();
      }
    }
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
