package com.a_nikhil.financier.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.DummyExpenditures;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.data.Set;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class SplineChartFragment extends Fragment {
    private static final String TAG = "SplineChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_spline_chart, container, false);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = new DummyExpenditures().getExpenditureDataAsList(1);
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

    @SuppressWarnings("all")
    private void createSplineChart(final View rootView, ArrayList<Expenditure> expenditures) {
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.spline_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.spline_progress_bar));
        Cartesian line = AnyChart.line();

        // Get month
        Date today = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        int month = Integer.parseInt(formatter.format(today).split("/")[1]);

        // Map Data of expenditures of current month
        HashMap<Integer, Double> dateMap = new HashMap<>(); // Day of Month : Total amount spent on that day
        for (Expenditure expenditure : expenditures) {
            int eDay = Integer.parseInt(expenditure.getDate().split("/")[0]); // expenditure day
            int eMonth = Integer.parseInt(expenditure.getDate().split("/")[1]); // expenditure month
            if (eMonth == month) {
                dateMap.put(eDay, dateMap.getOrDefault(eDay, 0d) + expenditure.getAmount());
            }
        }

        List<DataEntry> dataForSpline = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : dateMap.entrySet()) {
            dataForSpline.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }

        Set dataSet = Set.instantiate();
        dataSet.data(dataForSpline);
        dataSet.mapAs("{ x: 'x', value: 'value' }");
        line.spline(dataSet, "");
        line.animation(true);
        line.xScroller(true);
        line.title("For the month of - " + month);

        chartPlaceholder.setChart(line);
    }
}