package training.globant.myweather.presentation.show_weather.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 23/05/18.
 */


public class TabsPagerAdapter extends FragmentPagerAdapter {

  public TabsPagerAdapter(FragmentManager fm) {
    super(fm);
  }
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();


  @Override
  public Fragment getItem(int index) {
    return mFragmentList.get(index);
  }

  @Override
  public int getCount() {
    // get item count - equal to number of tabs
    return mFragmentList.size();
  }

  public void addFrag(Fragment fragment, String title) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }
}
