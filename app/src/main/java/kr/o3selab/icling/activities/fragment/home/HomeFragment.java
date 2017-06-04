package kr.o3selab.icling.activities.fragment.home;

import android.content.SharedPreferences;
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
import kr.o3selab.icling.activities.fragment.home.event.HomeDataListener;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.layout.FontFitTextView;
import kr.o3selab.icling.models.RidingData;
import kr.o3selab.icling.utils.SynchronizedData;
import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends BaseFragment implements HomeDataListener {

    @BindView(R.id.content_main_view_pager)
    ViewPager viewPager;
    @BindView(R.id.content_main_indicator)
    CircleIndicator indicator;

    @BindView(R.id.content_main_heart_rate)
    FontFitTextView heartRateView;
    @BindView(R.id.content_main_total_time)
    FontFitTextView totalTimeView;
    @BindView(R.id.content_main_total_distance)
    FontFitTextView totalDistanceView;

    SyncThread syncThread;
    HomeAdapter mAdapter;

    static final String MHR = "MHR";
    static final String MTT = "MTT";
    static final String MTD = "MTD";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        mAdapter = new HomeAdapter(getFragmentManager(), 1);

        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);

        SharedPreferences preferences = Constants.getSharedPreferences(getContext());
        int hr = preferences.getInt(MHR, 0);
        long tt = preferences.getLong(MTT, 0L);
        float td = preferences.getFloat(MTD, 0.0f);

        drawData(hr, tt, td);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("HOME");
        if (syncThread == null) {
            syncThread = new SyncThread();
            syncThread.start();
        }
    }

    public void drawData(int hr, long tt, double td) {
        RidingData.RTime rTime = RidingData.RTime.getTime(tt);

        String t = String.valueOf(rTime.h);
        if (rTime.h < 10) t += String.format("%.2f", rTime.m / 60.0).substring(1, 4);
        else t += String.format("%.1f", rTime.m / 60.0).substring(1, 3);
        heartRateView.setText(String.valueOf(hr));

        totalTimeView.setText(t);
        totalDistanceView.setText(String.format("%.2f", td));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (syncThread != null) syncThread.interrupt();
    }

    class SyncThread extends Thread {

        boolean isInterrupt = false;

        @Override
        public void run() {
            while (!isInterrupt) {
                if (SynchronizedData.syncFlag) {
                    SynchronizedData.setListener(HomeFragment.this);
                    SynchronizedData.sync(getContext());
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }

            syncThread = null;
        }

        @Override
        public void interrupt() {
            isInterrupt = true;
        }
    }

    @Override
    public void onSyncHomeData(final int hr, final long tt, final double td) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = Constants.getEditor(getContext());
                editor.putInt(MHR, hr);
                editor.putLong(MTT, tt);
                editor.putFloat(MTD, (float) td);
                editor.apply();

                drawData(hr, tt, td);

                mAdapter.drawKcalData();
            }
        });
    }
}
