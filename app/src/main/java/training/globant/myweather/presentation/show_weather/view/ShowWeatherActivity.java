package training.globant.myweather.presentation.show_weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import training.globant.myweather.R;

public class ShowWeatherActivity extends AppCompatActivity {

  private Toolbar toolbar;

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
  }

  private void initFragment(Fragment showWeatherFragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.container, showWeatherFragment);
    transaction.commit();
  }

}
