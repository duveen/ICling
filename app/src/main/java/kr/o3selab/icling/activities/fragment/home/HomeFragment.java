package kr.o3selab.icling.activities.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.activities.fragment.home.HomeAdapter;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.content_main_view_pager)
    ViewPager viewPager;
    @BindView(R.id.content_main_indicator)
    CircleIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), 3));
        indicator.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("HOME");
    }
}
