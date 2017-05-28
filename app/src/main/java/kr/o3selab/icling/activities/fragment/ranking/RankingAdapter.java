package kr.o3selab.icling.activities.fragment.ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RankingAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES = {"Easy", "Normal", "Hard"};
    int mNumbOfTabs;

    public RankingAdapter(FragmentManager fm, int numbOfTabs) {
        super(fm);
        this.mNumbOfTabs = numbOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {

        LevelFragment fragment = new LevelFragment();
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putString("map", "RaceRecord");
                bundle.putString("level", "Easy");
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                bundle.putString("map", "RaceRecord");
                bundle.putString("level", "Normal");
                fragment.setArguments(bundle);
                return fragment;
            default:
                bundle.putString("map", "RaceRecord");
                bundle.putString("level", "Hard");
                fragment.setArguments(bundle);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }

}
