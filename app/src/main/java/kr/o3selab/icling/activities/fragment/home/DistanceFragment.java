package kr.o3selab.icling.activities.fragment.home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.activities.fragment.home.event.HomeDistanceListener;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.common.GlobalApplication;
import kr.o3selab.icling.models.RidingData;
import kr.o3selab.icling.utils.Calendar;
import kr.o3selab.icling.utils.DBHelper;
import kr.o3selab.icling.utils.SynchronizedData;

public class DistanceFragment extends BaseFragment implements HomeDistanceListener {

    @BindView(R.id.main_page_distance_piechart)
    PieChart mDistanceChart;

    @BindView(R.id.main_page_distance_total)
    TextView mTotalDistanceView;

    private static final String TOTAL_DISTANCE = "total_distance";
    private static final String GOAL_DISTANCE = "goal_distance";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distance, null);

        ButterKnife.bind(this, view);
        initData();

        return view;
    }

    private void initData() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int chartMargin = (int) (metrics.widthPixels * 0.15);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDistanceChart.getLayoutParams();
        params.leftMargin = chartMargin;
        params.rightMargin = chartMargin;

        mDistanceChart.setLayoutParams(params);

        mDistanceChart.setData(getUserData());
        mDistanceChart.setUsePercentValues(false);
        mDistanceChart.getDescription().setEnabled(false);
        mDistanceChart.setDrawCenterText(false);
        mDistanceChart.setDrawHoleEnabled(true);
        mDistanceChart.setRotationEnabled(false);
        mDistanceChart.setTouchEnabled(false);

        mDistanceChart.setHoleRadius(92.0f);
        mDistanceChart.setHoleColor(Color.TRANSPARENT);

        mDistanceChart.getLegend().setEnabled(false);

        mDistanceChart.setUsePercentValues(!mDistanceChart.isUsePercentValuesEnabled());
        for (IDataSet<?> set : mDistanceChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        mDistanceChart.invalidate();

        SharedPreferences sharedPreferences = Constants.getSharedPreferences(getContext());
        Float totalKcal = sharedPreferences.getFloat(TOTAL_DISTANCE, 0.0f);
        mTotalDistanceView.setText(String.format("%.2f", totalKcal));
    }

    private PieData getUserData() {
        SharedPreferences preferences = Constants.getSharedPreferences(getContext());
        Float goalDistance = preferences.getFloat(GOAL_DISTANCE, 0.0f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (goalDistance >= 1) {
            entries.add(new PieEntry(goalDistance));
            entries.add(new PieEntry(0.0f));
        } else {
            entries.add(new PieEntry(goalDistance));
            entries.add(new PieEntry(1 - goalDistance));
        }

        PieDataSet dataSet = new PieDataSet(entries, "km");
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));

        dataSet.setColors(colors);

        return new PieData(dataSet);
    }

    public void drawData() {
        Float totalDistance = 0.0f;

        DBHelper helper = DBHelper.getInstance(getContext());
        Vector<RidingData> datas = helper.getRidingData();

        for (RidingData data : datas) {
            String nowWeek = Calendar.getWeek(System.currentTimeMillis());
            String dataWeek = Calendar.getWeek(data.mStartTime);
            if (nowWeek.equals(dataWeek)) totalDistance += data.mTotalDistance.floatValue();
        }

        Float goalDistance = totalDistance / Constants.user.mWeekDistance.floatValue();
        SharedPreferences.Editor editor = Constants.getEditor(GlobalApplication.getGlobalApplicationContext());
        editor.putFloat(TOTAL_DISTANCE, totalDistance);
        editor.putFloat(GOAL_DISTANCE, goalDistance);
        editor.apply();

        initData();
    }

    @Override
    public void onSyncDistanceData() {
        drawData();
    }

    @Override
    public void onResume() {
        super.onResume();
        SynchronizedData.getInstance().addHomeDistanceListener(this).sync(GlobalApplication.getGlobalApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        SynchronizedData.getInstance().removeHomeDistanceListener();
    }
}
