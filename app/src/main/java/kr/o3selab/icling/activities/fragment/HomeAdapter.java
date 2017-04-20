package kr.o3selab.icling.activities.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.o3selab.icling.activities.loaddata.BikeData;
import kr.o3selab.icling.activities.loaddata.FinalData;
import kr.o3selab.icling.activities.loaddata.LoginData;
import kr.o3selab.icling.activities.loaddata.UserData;

public class HomeAdapter extends FragmentStatePagerAdapter {

    int mNumbOfTabs;

    public HomeAdapter(FragmentManager fm, int numbOfTabs) {
        super(fm);

        this.mNumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            default:
                return new KcalFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }

}
