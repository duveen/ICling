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
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.RidingData;
import kr.o3selab.icling.utils.Calendar;
import kr.o3selab.icling.utils.DBHelper;
import kr.o3selab.icling.utils.Debug;

public class KcalFragment extends BaseFragment {

    @BindView(R.id.main_page_kcal_piechart)
    PieChart mKcalChart;

    @BindView(R.id.main_page_kcal_total)
    TextView mTotalKcalView;

    private static final String TOTAL_KCAL = "total_kcal";
    private static final String GOAL_KCAL = "goal_kcal";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcal, null);

        ButterKnife.bind(this, view);
        initData();

        return view;
    }

    private void initData() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int chartMargin = (int) (metrics.widthPixels * 0.15);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mKcalChart.getLayoutParams();
        params.leftMargin = chartMargin;
        params.rightMargin = chartMargin;

        mKcalChart.setLayoutParams(params);

        mKcalChart.setData(getUserData());
        mKcalChart.setUsePercentValues(false);
        mKcalChart.getDescription().setEnabled(false);
        mKcalChart.setDrawCenterText(false);
        mKcalChart.setDrawHoleEnabled(true);
        mKcalChart.setRotationEnabled(false);
        mKcalChart.setTouchEnabled(false);

        mKcalChart.setHoleRadius(92.0f);
        mKcalChart.setHoleColor(Color.TRANSPARENT);

        mKcalChart.getLegend().setEnabled(false);

        mKcalChart.setUsePercentValues(!mKcalChart.isUsePercentValuesEnabled());
        for (IDataSet<?> set : mKcalChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        mKcalChart.invalidate();

        SharedPreferences sharedPreferences = Constants.getSharedPreferences(getContext());
        Integer totalKcal = sharedPreferences.getInt(TOTAL_KCAL, 0);
        mTotalKcalView.setText(totalKcal.toString());
    }

    private PieData getUserData() {
        SharedPreferences preferences = Constants.getSharedPreferences(getContext());
        Float goalKcal = preferences.getFloat(GOAL_KCAL, 0.0f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(goalKcal));
        entries.add(new PieEntry(1 - goalKcal));

        PieDataSet dataSet = new PieDataSet(entries, "kcal");
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));

        dataSet.setColors(colors);

        return new PieData(dataSet);
    }

    public void drawData() {
        Integer totalKcal = 0;

        DBHelper helper = DBHelper.getInstance(getContext());
        Vector<RidingData> datas = helper.getRidingData();

        for (RidingData data : datas) {
            String nowWeek = Calendar.getWeek(System.currentTimeMillis());
            String dataWeek = Calendar.getWeek(data.mStartTime);
            if (nowWeek.equals(dataWeek)) totalKcal += data.mKcal;
        }

        Float goalKcal = totalKcal / Constants.user.mWeekKcal.floatValue();
        SharedPreferences.Editor editor = Constants.getEditor(getContext());
        editor.putInt(TOTAL_KCAL, totalKcal);
        editor.putFloat(GOAL_KCAL, goalKcal);
        editor.apply();

        initData();
    }
}
