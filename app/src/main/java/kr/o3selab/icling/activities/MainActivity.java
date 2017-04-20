package kr.o3selab.icling.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.AnalyticsFragment;
import kr.o3selab.icling.activities.fragment.HomeFragment;
import kr.o3selab.icling.activities.fragment.RecordFragment;
import kr.o3selab.icling.activities.fragment.SettingFragment;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.utils.Debug;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener {

    @Bind(R.id.main_top_bar)
    FrameLayout mTopBar;
    @Bind(R.id.main_title)
    TextView mMainTitle;

    @Bind(R.id.main_bottom_bar)
    BottomBar mBottomBar;

    /*@Bind(R.id.main_chart)
    LineChart mLineChart;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Debug.d("MainActivity onCreate");
        Debug.d("User = " + Constants.user.toString());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constants.DpToPixel(this, 56));
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        mTopBar.setLayoutParams(params);

        mBottomBar.setOnTabSelectListener(this);
        // initDataGraph();
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {

        switch (tabId) {
            case R.id.tab_home:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setActivity(this);
                addFragment(homeFragment);
                return;

            case R.id.tab_record:
                RecordFragment recordFragment = new RecordFragment();
                recordFragment.setActivity(this);
                addFragment(recordFragment);
                return;

            case R.id.tab_analytics:
                AnalyticsFragment analyticsFragment = new AnalyticsFragment();
                analyticsFragment.setActivity(this);
                addFragment(analyticsFragment);
                return;

            case R.id.tab_setting:
                SettingFragment settingFragment = new SettingFragment();
                settingFragment.setActivity(this);
                addFragment(settingFragment);
        }

    }

    public void addFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.getFragments() != null) {
            transaction.replace(R.id.main_container, newFragment);
        } else {
            transaction.add(R.id.main_container, newFragment);
        }

        transaction.commit();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void setTitle(CharSequence title) {
        mMainTitle.setText(title);
    }

    /*public void initDataGraph() {

        List<Entry> entries = new ArrayList<>();
        RidingData ridingData = new RidingData();
        int i = 0;
        for (Float d : ridingData.mDetailSpeed) {
            entries.add(new Entry(i++, d));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        if (Build.VERSION.SDK_INT > 23) {
            dataSet.setFillColor(getResources().getColor(R.color.colorPrimaryDark, null));
        } else {
            dataSet.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //LineData lineData = new LineData(dataSet);

        // 데이터 설정
        //mLineChart.setData(lineData);

        // 설명 해제
        mLineChart.getDescription().setEnabled(false);

        // 핀치 줌 해제
        mLineChart.setPinchZoom(false);

        // 조절 및 드래그 해제
        mLineChart.setScaleEnabled(false);
        mLineChart.setDragEnabled(false);

        mLineChart.setTouchEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setDrawGridBackground(false);

        XAxis x = mLineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setTextColor(Color.WHITE);

        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);

        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getLegend().setEnabled(false);

        mLineChart.setNoDataText("최근 5일간의 기록이 없습니다.");

        mLineChart.invalidate();
    }*/
}
