package kr.o3selab.icling.activities.loaddata;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LoadDataAdapter extends FragmentStatePagerAdapter {

    int mNumbOfTabs;

    public LoadDataAdapter(FragmentManager fm, int numbOfTabs) {
        super(fm);

        this.mNumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new LoginData();
            case 1:
                return new UserData();
            case 2:
                return new BikeData();
            default:
                return new FinalData();
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}
