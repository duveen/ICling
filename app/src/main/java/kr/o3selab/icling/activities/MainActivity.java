package kr.o3selab.icling.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.AnalyticsFragment;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.activities.fragment.home.HomeFragment;
import kr.o3selab.icling.activities.fragment.ranking.RankingFragment;
import kr.o3selab.icling.activities.fragment.record.RecordFragment;
import kr.o3selab.icling.activities.fragment.setting.SettingFragment;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.utils.Debug;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener {

    @BindView(R.id.main_top_bar)
    FrameLayout mTopBar;
    @BindView(R.id.main_back_button)
    View mBackButton;
    @BindView(R.id.main_title)
    TextView mMainTitle;

    @BindView(R.id.main_bottom_bar)
    BottomBar mBottomBar;

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
        Constants.synchronizedData(this);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) onBackPressed();

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

            case R.id.tab_ranking:
                RankingFragment rankingFragment = new RankingFragment();
                rankingFragment.setActivity(this);
                addFragment(rankingFragment);
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

    public void addFragment(BaseFragment newFragment) {
        addFragment(newFragment, false);
    }

    public void addFragment(BaseFragment newFragment, boolean isAdd) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (isAdd) {
            transaction.replace(R.id.main_container, newFragment, newFragment.TAG).addToBackStack(null);
        } else if (fragmentManager.getFragments() != null) {
            transaction.replace(R.id.main_container, newFragment);
        } else {
            transaction.add(R.id.main_container, newFragment);
        }

        transaction.commit();
    }

    public void removeFragment() {
        onBackPressed();
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

    public View getBackButton() {
        return mBackButton;
    }


}
