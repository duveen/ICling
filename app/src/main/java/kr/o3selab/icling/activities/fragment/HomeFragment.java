package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.o3selab.icling.R;
import me.relex.circleindicator.CircleIndicator;



public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        // activity.setTitle("Home");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.content_main_view_pager);
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), 3));

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.content_main_indicator);
        indicator.setViewPager(viewPager);

        return view;
    }

}
