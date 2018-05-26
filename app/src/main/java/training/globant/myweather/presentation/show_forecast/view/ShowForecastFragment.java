package training.globant.myweather.presentation.show_forecast.view;

import android.app.ProgressDialog;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
  private Map<String, String> lastQuery;
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
    setHasOptionsMenu(true);
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
        presenter.loadForecast(parameters);
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
    progressDialog.setTitle("Loading...");
    progressDialog.setMessage("Please wait.");
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
