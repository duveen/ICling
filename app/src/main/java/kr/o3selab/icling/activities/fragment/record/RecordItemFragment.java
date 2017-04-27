package kr.o3selab.icling.activities.fragment.record;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.models.RidingData;

public class RecordItemFragment extends BaseFragment {

    View mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_item, null);
        TAG = "RecordItemFragment";

        RidingData ridingData = (RidingData) getArguments().getSerializable("item");
        ArrayList<Float> list = ridingData.mDetailSpeed;

        list.add(0f);
        list.add(1f);
        list.add(2f);
        list.add(3f);
        list.add(4f);
        list.add(5f);
        list.add(6f);
        list.add(10f);
        list.add(15f);
        list.add(20f);
        list.add(25f);
        list.add(30f);
        list.add(32.5f);
        list.add(35f);
        list.add(35f);
        list.add(34f);
        list.add(35f);
        list.add(34f);
        list.add(35f);
        list.add(34.5f);
        list.add(34f);
        list.add(20f);
        list.add(17.5f);
        list.add(15f);
        list.add(15f);
        list.add(17f);
        list.add(18f);
        list.add(19f);
        list.add(20f);
        list.add(21f);
        list.add(22f);
        list.add(22.5f);
        list.add(23f);
        list.add(23f);
        list.add(23f);
        list.add(23f);
        list.add(23f);
        list.add(22.5f);
        list.add(22f);
        list.add(22f);
        list.add(21f);
        list.add(20f);
        list.add(19f);
        list.add(18f);
        list.add(17f);
        list.add(16f);
        list.add(15f);
        list.add(15f);
        list.add(16f);
        list.add(17f);
        list.add(18f);
        list.add(19f);
        list.add(19.5f);
        list.add(20f);
        list.add(20.5f);
        list.add(21f);
        list.add(21.5f);
        list.add(22f);
        list.add(22.5f);
        list.add(23f);
        list.add(23.5f);
        list.add(24f);
        list.add(24.5f);
        list.add(25f);
        list.add(25f);
        list.add(25f);
        list.add(24.5f);
        list.add(24f);
        list.add(24f);
        list.add(23.5f);
        list.add(23f);
        list.add(22.5f);
        list.add(22f);
        list.add(21f);
        list.add(20.5f);
        list.add(20f);
        list.add(20f);
        list.add(20f);
        list.add(19.5f);
        list.add(19f);
        list.add(18.5f);
        list.add(18f);
        list.add(17.8f);
        list.add(17.5f);
        list.add(17.3f);
        list.add(17f);
        list.add(17.3f);
        list.add(17.5f);
        list.add(17.7f);
        list.add(18f);
        list.add(18.3f);
        list.add(18.4f);
        list.add(18.5f);
        list.add(18.5f);
        list.add(18.6f);
        list.add(18.7f);
        list.add(18.8f);

        List<Entry> entries = new ArrayList<>();

        int i = 0;
        for(Float f : list) entries.add(new Entry(i++, f));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) dataSet.setFillColor(getResources().getColor(R.color.colorPrimary, null));
        else dataSet.setFillColor(getResources().getColor(R.color.colorPrimary));

        LineData lineData = new LineData(dataSet);

        LineChart lineChart = (LineChart) view.findViewById(R.id.record_item_chart);
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