package training.globant.myweather.presentation.show_weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import training.globant.myweather.R;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;

public class ShowWeatherActivity extends AppCompatActivity {
  //You must return true for the menu to be displayed; if you return false it will not be shown.
  private static final boolean SHOW_MENU = true;
  private static final String SHOW_WEATHER_TAG = "SHOW_WEATHER_FRAGMENT";

  private Toolbar toolbar;
  private SimpleCursorAdapter mAdapter;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_weather);
    setUpToolbar();
    if (savedInstanceState == null) {
      initFragment(ShowWeatherFragment.newInstance());
    }
  }

  private void setUpToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    final int[] to = new int[] {android.R.id.text1};
    final String[] from = new String[] {"cityName"};
    mAdapter = new SimpleCursorAdapter(this,
        R.layout.search_view_suggestion_row,
        null,
        from,
        to,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
  }

  private void initFragment(Fragment showWeatherFragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.container, showWeatherFragment, SHOW_WEATHER_TAG);
    transaction.commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    final MenuItem mSearchItem = (MenuItem) menu.findItem(R.id.m_search);

    //https://stackoverflow.com/questions/45335853/cant-set-onactionexpandlistener-this-is-not-supported-use-menuitemcompat-seto/45431091
    final SearchView searchView = (SearchView) mSearchItem.getActionView();
    searchView.setSuggestionsAdapter(mAdapter);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String textSubmitted) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SHOW_WEATHER_TAG);
        if(fragment != null && fragment instanceof ShowWeatherContract.View){
          ShowWeatherContract.View showWeatherView = (ShowWeatherContract.View)fragment;
          showWeatherView.searchWeather(textSubmitted);
        }
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

    return SHOW_MENU;
  }

}
