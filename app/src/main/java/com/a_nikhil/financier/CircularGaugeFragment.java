package com.a_nikhil.financier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

public class CircularGaugeFragment extends Fragment {

    private static final String TAG = "CircularGaugeFragment";

    private View rootView;
    private ArrayList<Expenditure> expenditures;
    private double maxIncome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_circular_gauge, container, false);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        expenditures = new DummyExpenditures().getExpenditureDataAsList();
        maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        maxIncome = 30000d;
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }

        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);

        // Get month
        Date today = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        int month = Integer.parseInt(formatter.format(today).split("/")[1]);
        Log.d(TAG, "onCreateView: Month = " + month);

        createCircularGauge(month);
        return rootView;
    }

    private void createCircularGauge(int month) {
        // Initializing data
        double[] categorySum = new double[8];
        double total = 0.0d;

        // Calculating total
        Map<String, Double> costMap = new HashMap<>();
        for (Expenditure expenditure : expenditures) {
            int eMonth = Integer.parseInt(expenditure.getDate().split("/")[1]); // expenditureMonth -> eMonth
            if (eMonth == month) {
                String category = expenditure.getCategory().toString();
                costMap.put(category, costMap.getOrDefault(category, 0d) + expenditure.getAmount());
            }
        }

        List<Map.Entry<String, Double>> costMapEntrySet = new LinkedList<>(costMap.entrySet());
        costMapEntrySet.sort(Map.Entry.comparingByValue());

        // Get percentages
        List<Integer> data = new ArrayList<>();
        for (double sum : categorySum) {
            if (sum != 0) {
                Log.d(TAG, "\ncreateCircularGauge: sum = " + sum + " maxIncome = " + maxIncome);
                data.add(((int)sum * 100) / (int)maxIncome);
            }
        }

        Log.d(TAG, "\ncreateCircularGauge: savings = " + (maxIncome - total) + " maxIncome = " + maxIncome);
        data.add((int)(maxIncome - total) * 100 / (int)maxIncome);
        data.add(100); // 100%

        // Creating circular gauge
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.circular_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.circular_progress_bar));
        CircularGauge circular = AnyChart.circular();

        Log.d(TAG, "createCircularGauge: Data = " + data);
//        circular.data(new SingleValueDataSet(data.toArray(new Double[0])));

        circular.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d);
        circular.startAngle(0d);
        circular.sweepAngle(270d);

        chartPlaceholder.setChart(circular);
    }
}