package kr.o3selab.icling.activities.fragment.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomeAdapter extends FragmentStatePagerAdapter {

    int mNumbOfTabs;

    KcalFragment kcalFragment;
    DistanceFragment distanceFragment;

    public HomeAdapter(FragmentManager fm, int numbOfTabs) {
        super(fm);

        this.mNumbOfTabs = numbOfTabs;
        kcalFragment = new KcalFragment();
        distanceFragment = new DistanceFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return kcalFragment;
            case 1:
                return distanceFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}
