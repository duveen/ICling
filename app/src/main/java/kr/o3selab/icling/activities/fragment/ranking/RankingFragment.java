package kr.o3selab.icling.activities.fragment.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.o3selab.icling.layout.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;

public class RankingFragment extends BaseFragment {

    @BindView(R.id.ranking_view_pager)
    ViewPager viewPager;
    @BindView(R.id.ranking_indicator)
    PagerSlidingTabStrip indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, null);

        ButterKnife.bind(this, view);

        viewPager.setAdapter(new RankingAdapter(getFragmentManager(), 3));
        viewPager.setOffscreenPageLimit(3);
        indicator.setViewPager(viewPager);
        indicator.setTextColor(R.color.colorPrimary);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("랭킹");
    }
}
