package com.a_nikhil.financier.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Spline;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class SplineChartFragment extends Fragment {
    private static final String TAG = "SplineChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_spline_chart, container, false);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
        final double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);
        createSplineChart(rootView, expenditures);
        return rootView;
    }

    private void createSplineChart(final View rootView, ArrayList<Expenditure> expenditures) {
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.spline_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.spline_progress_bar));
        Cartesian line = AnyChart.line();

        List<DataEntry> myData = new ArrayList<>();
        myData.add(new ValueDataEntry(1, 10));
        myData.add(new ValueDataEntry(2, 20));
        myData.add(new ValueDataEntry(3, 40));
        myData.add(new ValueDataEntry(4, 60));
        myData.add(new ValueDataEntry(5, 10));
        myData.add(new ValueDataEntry(6, 50));
        myData.add(new ValueDataEntry(7, 5));

        Set dataSet = Set.instantiate();
        dataSet.data(myData);
        Mapping dataMapping = dataSet.mapAs("{ x: 'x', value: 'value' }");
        Spline spline = line.spline(dataSet, "");
        line.legend(true);
        line.animation(true);
        line.title("Test");

        chartPlaceholder.setChart(line);
    }
}