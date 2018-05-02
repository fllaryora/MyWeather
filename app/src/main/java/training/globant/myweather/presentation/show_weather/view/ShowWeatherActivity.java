package training.globant.myweather.presentation.show_weather.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import training.globant.myweather.R;

public class ShowWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_weather);
        setUpToolbar();

        if (savedInstanceState == null) {
            initFragment(ShowWeatherFragment.newInstance());
        }
        SharedPreferences preferences = this.getSharedPreferences(
                "training.globant.myweather", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("q","Córdoba,AR");
        editor.putString("APPID","XXXXXXXXXXXXXXXXXXX");
        editor.commit();
        //editor.apply(); // asynchronous commit.
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        // TODO Use Toolbar?
    }

    private void initFragment(Fragment showWeatherFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, showWeatherFragment);
        transaction.commit();
    }
}
