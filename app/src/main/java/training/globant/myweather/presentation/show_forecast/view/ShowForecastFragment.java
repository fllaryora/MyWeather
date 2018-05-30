package training.globant.myweather.presentation.show_forecast.view;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import training.globant.myweather.R;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.PermissionHelperCallback;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.presentation.show_forecast.ShowForecastContract;
import training.globant.myweather.presentation.show_forecast.model.CityUI;
import training.globant.myweather.presentation.show_forecast.presenter.ShowForecastPresenter;

/**
 * Represents a View in a model view presenter (MVP) pattern.
 * In this case is used as a view of {@link training.globant.myweather.presentation.show_forecast.model.ForecastItemUI}.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ShowForecastFragment extends Fragment implements ShowForecastContract.View{

  private ShowForecastPresenter presenter;
  private PermissionsHelper permissionsHelper;
  private ProgressDialog progressDialog;
  private SwipeRefreshLayout swipeRefreshLayout;
  private TextView hintLabel;
  private TextView city;
  private PermissionHelperCallback helperCallback;
  private RecyclerView recyclerView;
  private ForecastAdapter forecastAdapter;

  public static ShowForecastFragment newInstance() {
    return new ShowForecastFragment();
  }

  /***************** ANDROID LIFE-CYCLE FUNCTIONS ************************/
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new ShowForecastPresenter();
    presenter.attachView(this);
    permissionsHelper = new PermissionsHelper(this);
    progressDialogSetup();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_show_forecast, container, false);
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    hintLabel = (TextView) view.findViewById(R.id.text_empty);
    city = (TextView) view.findViewById(R.id.cityLabel);
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    createHelperPermissionCallBack();
    setUpSwipeToRefresh();
    if (savedInstanceState != null) {
      CityUI uiModel = savedInstanceState.getParcelable(Constant.KEY_FORECAST);
      presenter.restoreStateAndShowForecast(uiModel);
    }
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      CityUI uiModel = savedInstanceState.getParcelable(Constant.KEY_FORECAST);
      presenter.restoreStateAndShowForecast(uiModel);
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
  public void onLocationChange() {
    progressDialog.show();
    SharedPreferences sharedPref = getActivity().getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE);
    Map<String, String> lastQuery = new HashMap<String, String>();
    lastQuery.put(Constant.API_PARAMETER_QUERY,sharedPref.getString(Constant.API_PARAMETER_QUERY,null));
    presenter.loadForecast(lastQuery);
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
    outState.putParcelable(Constant.KEY_FORECAST, presenter.getUiModel());
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

  private void progressDialogSetup() {
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setTitle(getString(R.string.loading));
    progressDialog.setMessage(getString(R.string.please_wait));
    progressDialog.setCancelable(false);
  }

  private void createHelperPermissionCallBack() {
    helperCallback = new PermissionHelperCallback() {
      @Override
      public void onRequestPermissionsResultFail(int requestCode,
          @NonNull String[] permissions, @NonNull int[] grantResults) {
        ShowForecastFragment.super
            .onRequestPermissionsResult(requestCode, permissions, grantResults);
      }

      @Override
      public void onResponse() {
        progressDialog.show();
        presenter.loadForecast(null);
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

  private void setUpSwipeToRefresh() {
    swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE);
            Map<String, String> lastQuery = new HashMap<String, String>();
            lastQuery.put(Constant.API_PARAMETER_QUERY,sharedPref.getString(Constant.API_PARAMETER_QUERY,null));
            presenter.refreshForecast(lastQuery);
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
   * Shows uiModel Forecast in the view
   *
   * @param uiModel is the view model
   */
  @Override
  public void showForecast(CityUI uiModel) {
    //TODO for the love of god bring to another fragment the message when I have time
    hintLabel.setVisibility(View.GONE);
    swipeRefreshLayout.setVisibility(View.VISIBLE);
    city.setText(uiModel.getCityLabel());

    LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    //Adding a divider between rows
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        layoutManager.getOrientation());
    recyclerView.addItemDecoration(dividerItemDecoration);

    forecastAdapter = new ForecastAdapter(this.getActivity(), uiModel.getForecastItemUIList());
    recyclerView.setAdapter(forecastAdapter);

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
      Snackbar
          .make(showWeatherView, (error != null) ? error : getString(R.string.can_not_load_message),
              Snackbar.LENGTH_LONG).show();
    }
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
