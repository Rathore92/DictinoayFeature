package listeners;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import fragmentssample.fragment.FkMapFragment;

/**
 * Created by lal.chand on 21/08/14.
 */
public class ViewPagerListener implements ViewPager.OnPageChangeListener {

    private boolean isJustAfterResume = false;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        FkMapFragment current = new FkMapFragment();
        if(position == 2){
            Log.e("from onPageSelected", "" + position);
            current.onPageSelected();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
