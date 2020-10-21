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
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        double[] categoryPercentage = new double[9];
        double sum = 0.0d;

        // Calculating total
        for (Expenditure expenditure : expenditures) {
            if (Integer.parseInt(expenditure.getDate().split("/")[1]) == month) {
                categoryPercentage[expenditure.getCategory().getIndex() - 1] += expenditure.getAmount();
                sum += expenditure.getAmount();
            }
        }
        categoryPercentage[8] = maxIncome - sum; // savings

        // Get percentages
        List<Double> data = new ArrayList<>();
        for (double percentage : categoryPercentage) {
            if (percentage != 0) {
                data.add((percentage * 100 / maxIncome));
            }
        }

        // Creating circular gauge
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.circular_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.circular_progress_bar));

        CircularGauge circular = AnyChart.circular();
        Log.d(TAG, "createCircularGauge: " + data);
        circular.data(new SingleValueDataSet(data.toArray(new Double[0])));

        circular.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d);
        circular.startAngle(0d);
        circular.sweepAngle(270d);

        chartPlaceholder.setChart(circular);
    }
}