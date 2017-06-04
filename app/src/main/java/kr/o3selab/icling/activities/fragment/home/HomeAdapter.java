package kr.o3selab.icling.activities.fragment.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomeAdapter extends FragmentStatePagerAdapter {

    int mNumbOfTabs;

    KcalFragment kcalFragment;

    public HomeAdapter(FragmentManager fm, int numbOfTabs) {
        super(fm);

        this.mNumbOfTabs = numbOfTabs;
        kcalFragment = new KcalFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            default:
                return kcalFragment;
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }

    public void drawKcalData() {
        kcalFragment.drawData();
    }


}
