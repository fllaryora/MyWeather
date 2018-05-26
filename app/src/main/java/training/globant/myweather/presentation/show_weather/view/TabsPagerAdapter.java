package training.globant.myweather.presentation.show_weather.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the adapter to populate pages. Associates each page with a key Object. Supports data set
 * changes. Data set changes must occur on the main thread and must end with a call to {@link
 * #notifyDataSetChanged()}. When pages are not visible to the user, their entire fragment may be
 * destroyed, only keeping the saved state of that fragment.
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  /**
   * Constructor of TabsPagerAdapter
   *
   * @param fm Static library support version of the framework's
   */
  public TabsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  /**
   * Return the Fragment associated with a specified position.
   *
   * @param index The position of the Item requested
   * @return A Fragment for the requested index
   */
  @Override
  public Fragment getItem(int index) {
    return mFragmentList.get(index);
  }

  /**
   * Return the number of views available.
   */
  @Override
  public int getCount() {
    return mFragmentList.size();
  }

  /**
   * Adds a Fragment Page
   *
   * @param fragment page
   * @param title A title for the page
   */
  public void addPage(Fragment fragment, String title) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }

  /**
   * Deletes a Fragment Page
   *
   * @param position The position of the fragment page to delete
   */
  void deletePage(int position) {
    mFragmentList.remove(position);
    mFragmentTitleList.remove(position);
    notifyDataSetChanged();
  }

  /**
   * This method may be called by the ViewPager to obtain a title string to describe the specified
   * page. This method may return null indicating no title for this page. The default implementation
   * returns null.
   *
   * It is used to set the text shown on the tab in a TabLayout .
   *
   * @param position The position of the title requested
   * @return A title for the requested page
   */
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }

}
