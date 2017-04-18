package kr.o3selab.icling.activities.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import kr.o3selab.icling.R;

public class KcalFragment extends Fragment {

    public KcalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcal, null);

        PieChart pieChart = (PieChart) view.findViewById(R.id.main_page_kcal_piechart);

        pieChart.setData(setData());
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationEnabled(false);

        pieChart.setHoleRadius(95.0f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.invalidate();

        return view;
    }

    private PieData setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.2f));
        entries.add(new PieEntry(0.8f));

        PieDataSet dataSet = new PieDataSet(entries, "kcal");
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        return data;
    }
}
