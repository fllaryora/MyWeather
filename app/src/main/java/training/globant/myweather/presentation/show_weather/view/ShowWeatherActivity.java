package training.globant.myweather.presentation.show_weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import training.globant.myweather.R;
import training.globant.myweather.presentation.show_forecast.view.ShowForecastFragment;

/**
 * Represents a Activity that contains the main fragments
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class ShowWeatherActivity extends AppCompatActivity {

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
}
