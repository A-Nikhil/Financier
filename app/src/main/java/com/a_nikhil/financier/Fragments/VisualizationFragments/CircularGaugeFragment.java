package com.a_nikhil.financier.Fragments.VisualizationFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.DummyExpenditures;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.CategoryColorMap;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        createCircularGauge(month);
        return rootView;
    }

    @SuppressWarnings("all")
    private void createCircularGauge(int month) {
        // Initializing data
        double total = 0.0d;

        // Calculating total
        Map<String, Integer> costPercentageMap = new HashMap<>();
        for (Expenditure expenditure : expenditures) {
            int eMonth = Integer.parseInt(expenditure.getDate().split("/")[1]); // expenditureMonth -> eMonth
            if (eMonth == month) {
                String category = expenditure.getCategory().toString();
                double amount = expenditure.getAmount();
                total += amount;
                costPercentageMap.put(category,
                        costPercentageMap.getOrDefault(category, 0) + (int) (amount * 100 / maxIncome));
            }
        }
        int savings = ((int) ((maxIncome - total) * 100 / maxIncome));

        List<Integer> values = new ArrayList<>(costPercentageMap.values());
        Collections.sort(values); // sort values
        String[] dataForGauge = new String[values.size() + 2];
        int i = 0;
        for (int value : values) {
            dataForGauge[i++] = String.valueOf(value);
        }
        dataForGauge[i++] = String.valueOf(String.valueOf(savings)); // add savings
        dataForGauge[i++] = "100"; // Add 100 for percentage

        List<Map.Entry<String, Integer>> costMapEntrySet = new LinkedList<>(costPercentageMap.entrySet());
        costMapEntrySet.sort(Map.Entry.comparingByValue());
        costMapEntrySet.add(new AbstractMap.SimpleEntry<>("Savings", (int) ((maxIncome - total) * 100 / maxIncome)));

        // Creating circular gauge
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.circular_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.circular_progress_bar));
        CircularGauge circularGauge = AnyChart.circular();

        // Set Gauge Init Params
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d)
                .startAngle(0d)
                .sweepAngle(270d);

        // Chart Axes data
        Circular xAxis = circularGauge.axis(0)
                .radius(100d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d);
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);

        // Add data to Gauge
        circularGauge.data(new SingleValueDataSet(dataForGauge));

        // Customize Gauge bars and text
        Bar dataBar, dataComplimentBar;
        for (int index = 1; index <= dataForGauge.length - 1; index++) {

            // Gathering placeholder data
            String title = costMapEntrySet.get(index - 1).getKey();
            String percentage = String.valueOf(costMapEntrySet.get(index - 1).getValue());
            String color = title.equals("Savings") ? "#424949" :
                    new CategoryColorMap().getCategoryColor(Category.valueOf(title.toUpperCase()));

            // Setting label
            circularGauge.label(index - 1)
                    .text(title + ", <span style=\"\">" + percentage + "%</span>")
                    .useHtml(true)
                    .hAlign(HAlign.CENTER)
                    .vAlign(VAlign.MIDDLE)
                    .anchor(Anchor.RIGHT_CENTER)
                    .padding(0d, 10d, 0d, 0d)
                    .height(17d / 2d + "%")
                    .offsetY((index * 20d) + "%")
                    .offsetX(0d);

            // Setting the data bar
            dataBar = circularGauge.bar(index - 1);
            dataBar.dataIndex(index - 1);
            dataBar.radius(index * 20d);
            dataBar.width(17d);
            dataBar.fill(new SolidFill(color, 1d));
            dataBar.stroke(null);
            dataBar.zIndex(5d);

            // Setting the data compliment bar
            dataComplimentBar = circularGauge.bar(100d + (index - 1));
            dataComplimentBar.dataIndex(dataForGauge.length - 1);
            dataComplimentBar.radius(index * 20d);
            dataComplimentBar.width(17d);
            dataComplimentBar.fill(new SolidFill("#F5F4F4", 1d));
            dataComplimentBar.stroke("1 #e5e4e4");
            dataComplimentBar.zIndex(4d);
        }

        // Setting Gauge Title
        circularGauge.margin(50d, 50d, 50d, 50d);
        circularGauge.title()
                .text("Expenditure (Category wise)' +\n" +
                        "    '<br/><span style=\"color:#929292; font-size: 12px;\">" +
                        "(for the month of + " + month + ")</span>")
                .useHtml(true);
        circularGauge.title().enabled(true);
        circularGauge.title().hAlign(HAlign.CENTER);
        circularGauge.title()
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 20d, 0d);

        chartPlaceholder.setChart(circularGauge);
    }
}