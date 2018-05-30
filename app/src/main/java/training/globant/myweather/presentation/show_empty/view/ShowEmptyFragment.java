package training.globant.myweather.presentation.show_empty.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import training.globant.myweather.R;
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
  private PermissionHelperCallback helperCallback;
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
    View view = inflater.inflate(R.layout.fragment_empty_weather_view, container, false);
    createHelperPermissionCallBack();
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
    permissionsHelper.tryLocation(helperCallback);
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
    permissionsHelper.onRequestPermissionsResult(requestCode,permissions,grantResults,helperCallback);

  }

  /***************** HELPER LIFE-CYCLE FUNCTIONS ************************/

  private void progressDialogSetup() {
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please wait.");
    progressDialog.setCancelable(false);
  }

  private void createHelperPermissionCallBack(){
    helperCallback = new PermissionHelperCallback() {
      @Override
      public void onRequestPermissionsResultFail(int requestCode,
          @NonNull String[] permissions, @NonNull int[] grantResults) {
        ShowEmptyFragment.super.onRequestPermissionsResult( requestCode, permissions, grantResults);
      }

      @Override
      public void onResponse() {
        progressDialog.show();
        presenter.goToWeather(null);
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
      Snackbar.make(showWeatherView, (error != null)?error: getString(R.string.can_not_load_message) , Snackbar.LENGTH_LONG).show();
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
