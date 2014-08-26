package fragmentssample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;

import listeners.ViewPagerListener;

public  class MyActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
    //private ViewPagerListener listener;

    GoogleMapFragment googleMapFragment = new GoogleMapFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        //listener = new ViewPagerListener();
        //pager.setOnPageChangeListener(listener);
        pager.setOnPageChangeListener(this);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 1){
            googleMapFragment.onPageSelected();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(android.support.v4.app.FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {
                case 0: {
                    Log.e("sdfasfsfasfsfsf 0"," : " + pos);
                    return FirstFragment.newInstance("FirstFragment, Instance 1");
                }
                case 1:{Log.e("sdfsdfsdafdsafdsaf 1"," : " + pos);
                    return SecondFragment.newInstance("SecondFragment, Instance 1");
                }
                case 2 : { Log.e("sdfafdsfadsfdasfadsfadsfd 2", " : " + pos);
                    return googleMapFragment.newInstance("bangalore");
                }
//                case 2: return new SupportMapFragment().newInstance("India");
                //return ThirdFragment.newInstance("sfdfsfsdfsfsfsfsfsdfs");
                default: return FirstFragment.newInstance("FirstFragment, Instance 1");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
