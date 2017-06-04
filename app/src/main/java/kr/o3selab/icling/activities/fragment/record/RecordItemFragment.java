package kr.o3selab.icling.activities.fragment.record;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.models.RidingData;

public class RecordItemFragment extends BaseFragment {

    @BindView(R.id.record_item_top_date)
    TextView topDate;

    @BindView(R.id.record_item_total_time_hour)
    TextView hourView;
    @BindView(R.id.record_item_total_time_hour_text)
    TextView hourTextView;
    @BindView(R.id.record_item_total_time_minute)
    TextView minuteView;
    @BindView(R.id.record_item_total_time_minute_text)
    TextView minuteTextView;
    @BindView(R.id.record_item_total_time_second)
    TextView secondView;
    @BindView(R.id.record_item_total_time_second_text)
    TextView secondTextView;

    @BindView(R.id.record_item_total_distance)
    TextView distanceView;
    @BindView(R.id.record_item_total_speed)
    TextView speedView;

    @BindView(R.id.record_item_chart)
    LineChart lineChart;

    @BindView(R.id.record_item_detail_time)
    TextView detailTimeView;
    @BindView(R.id.record_item_detail_distance)
    TextView detailDistanceView;
    @BindView(R.id.record_item_detail_kcal)
    TextView detailKcalView;
    @BindView(R.id.record_item_detail_average_heart)
    TextView detailAverageHeartView;
    @BindView(R.id.record_item_detail_max_heart)
    TextView detailMaxHeartView;
    @BindView(R.id.record_item_detail_average_speed)
    TextView detailAverageSpeedView;
    @BindView(R.id.record_item_detail_max_speed)
    TextView detailMaxSpeedView;
    @BindView(R.id.record_item_detail_pace)
    View detailPaceView;
    @BindView(R.id.record_item_detail_average_pace)
    TextView detailAveragePaceView;
    @BindView(R.id.record_item_detail_max_pace)
    TextView detailMaxPaceView;

    View mBackButton;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("M월 d일 (E)");
    public SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_item, null);
        TAG = "RecordItemFragment";

        ButterKnife.bind(this, view);

        RidingData ridingData = (RidingData) getArguments().getSerializable("item");

        // 상위 시간 뷰
        topDate.setText(dateFormat.format(new Date(ridingData.mStartTime)) + " " + timeFormat.format(new Date(ridingData.mStartTime)) + " - " + timeFormat.format(new Date(ridingData.mFinishTime)));

        // 상위 총 운동시간 및 정보
        RidingData.RTime totalTime = ridingData.getTotalTime();
        if (totalTime.h != 0) {
            hourView.setText(String.valueOf(totalTime.h));
            hourView.setVisibility(View.VISIBLE);
            hourTextView.setVisibility(View.VISIBLE);
        }

        if (totalTime.m != 0) {
            minuteView.setText(String.valueOf(totalTime.m));
            minuteView.setVisibility(View.VISIBLE);
            minuteTextView.setVisibility(View.VISIBLE);
        }

        if (totalTime.h == 0) {
            secondView.setText(String.valueOf(totalTime.s));
            secondView.setVisibility(View.VISIBLE);
            secondTextView.setVisibility(View.VISIBLE);
        }

        distanceView.setText(String.format("%.2f", ridingData.mTotalDistance));
        speedView.setText(String.format("%.2f", ridingData.mAverageSpeed));

        // 속도 그래프
        if (ridingData.mDetailSpeed != null) {
            List<Entry> entries = new ArrayList<>(ridingData.mDetailSpeed.keySet().size());
            List<String> lists = new ArrayList<>(ridingData.mDetailSpeed.keySet());
            Collections.sort(lists);

            for (String s : lists) {
                Long time = (Long.parseLong(s) - ridingData.mStartTime) / 1000;
                entries.add(new Entry(time.floatValue(), ridingData.mDetailSpeed.get(s).floatValue()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawFilled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                dataSet.setFillColor(getResources().getColor(R.color.colorPrimary, null));
            else dataSet.setFillColor(getResources().getColor(R.color.colorPrimary));

            LineData lineData = new LineData(dataSet);

            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false);
            lineChart.setPinchZoom(false);

            lineChart.setScaleEnabled(false);
            lineChart.setDragEnabled(false);
            lineChart.setTouchEnabled(false);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDrawGridBackground(false);

            XAxis x = lineChart.getXAxis();
            x.setEnabled(false);

            /*x.setDrawGridLines(true);
            x.setDrawAxisLine(false);*/

            lineChart.getAxisLeft().setTextColor(Color.WHITE);
            lineChart.getAxisRight().setEnabled(false);

            lineChart.getLegend().setEnabled(false);
            lineChart.invalidate();
        } else {
            lineChart.setVisibility(View.GONE);
        }


        // 세부 내용 설정
        detailTimeView.setText(ridingData.getRidingTime().toString());
        detailDistanceView.setText(String.format("%.2f", ridingData.mTotalDistance));
        detailKcalView.setText(String.valueOf(ridingData.mKcal));
        detailAverageHeartView.setText(String.valueOf(ridingData.mAverageHeartRate));
        detailMaxHeartView.setText(String.valueOf(ridingData.mMaxHeartRate));
        detailAverageSpeedView.setText(String.format("%.2f", ridingData.mAverageSpeed));
        detailMaxSpeedView.setText(String.format("%.2f", ridingData.mMaxSpeed));
        if (ridingData.getAveragePace() != null) {
            detailAveragePaceView.setText(ridingData.getAveragePace());
            detailMaxPaceView.setText(ridingData.getMaxPace());
        } else detailPaceView.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableBackButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBackButton.setVisibility(View.GONE);
        mBackButton.setOnClickListener(null);
    }

    void enableBackButton() {
        mBackButton = activity.getBackButton();
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeFragment();
            }
        });
        activity.setTitle("주행기록");
    }
}