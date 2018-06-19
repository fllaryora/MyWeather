package training.globant.myweather.presentation.show_weather.view;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.HashMap;
import java.util.Map;
import training.globant.myweather.R;
import training.globant.myweather.data.database.DatabaseTransformer;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.presentation.show_forecast.ShowForecastContract;
import training.globant.myweather.presentation.show_forecast.view.ShowForecastFragment;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;

/**
 * Represents a Activity that contains the main fragments
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class ShowWeatherActivity extends AppCompatActivity {

  private static final boolean SHOW_MENU = true;
  private Toolbar toolbar;
  private ViewPager viewPager;
  private TabsPagerAdapter mAdapter;
  private TabLayout tabLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_weather);
    setUpToolbar();
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    setupViewPager(viewPager);
    tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  private void setUpToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  private void setupViewPager(ViewPager viewPager) {
    mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

    mAdapter.addPage(ShowWeatherFragment.newInstance(), getString(R.string.tab_label_today));
    mAdapter.addPage(ShowForecastFragment.newInstance(), getString(R.string.tab_label_forecast));

    viewPager.setAdapter(mAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu_main, menu);
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
        Editor editor = getSharedPreferences(Constant.KEY_LAST_SEARCH, MODE_PRIVATE).edit();
        editor.putString(Constant.API_PARAMETER_QUERY, textSubmitted);
        //invalidate both models
        editor.putBoolean(Constant.KEY_WEATHER_MODEL_VALID, Constant.INVALID);
        editor.putBoolean(Constant.KEY_FORECAST_MODEL_VALID, Constant.INVALID);
        editor.commit();
        for (Fragment fragment : getSupportFragmentManager().getFragments()){
          if(fragment.isVisible()){
            if (fragment instanceof ShowForecastContract.View) {
              ((ShowForecastContract.View) fragment).onLocationChange();
            } else if (fragment instanceof ShowWeatherContract.View) {
              ((ShowWeatherContract.View) fragment).onLocationChange();
            }
          }
        }

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

    return SHOW_MENU;
  }
}
