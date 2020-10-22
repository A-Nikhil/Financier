package com.a_nikhil.financier.Fragments.VisualizationFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.CategoryColorMap;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Scatter;
import com.anychart.core.scatter.series.Marker;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.text.HAlign;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScatterChartFragment extends Fragment {

    private static final String TAG = "ScatterChartFragment";
    private View rootView;
    private ArrayList<Expenditure> expenditures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_scatter_chart, container, false);

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        expenditures = inputBundle.getParcelableArrayList("expenditures");
        /* Uncomment for testing
        expenditures = new DummyExpenditures().getExpenditureDataAsList(1.0d);
         */
        final double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);

        setScatterChart();
        return rootView;
    }

    private void setScatterChart() {
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.scatter_chart_view);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.scatter_progress_bar));

        HashMap<Category, String> colorMap = new CategoryColorMap().getColorMap();

        // Get month
        Date today = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        int month = Integer.parseInt(formatter.format(today).split("/")[1]);
        Log.d(TAG, "onCreateView: Month = " + month);

        List<DataEntry> dataForScatter = new ArrayList<>();
        for (Expenditure expenditure : expenditures) {
            // Check for month
            String[] date = expenditure.getDate().split("/");
            if (date[1].equals(String.valueOf(month))) {
//                dataForScatter.add(new ValueDataEntry(Integer.parseInt(date[0]), expenditure.getAmount()));
                dataForScatter.add(new CustomScatterData(
                        Integer.parseInt(date[0]),
                        expenditure.getAmount(),
                        colorMap.get(expenditure.getCategory()),
                        expenditure.getTitle())
                );
            }
        }

        Scatter scatter = AnyChart.scatter();

        scatter.animation(true);
        scatter.title("Category Wise Expenditures");

        // Setting bounds
        scatter.xScale().minimum(0d).maximum(31d);

        scatter.yAxis(0).title("Amount");
        scatter.xAxis(0).title("Day of the month");

        scatter.interactivity()
                .hoverMode(HoverMode.BY_SPOT)
                .spotRadius(30d);

        scatter.tooltip().displayMode(TooltipDisplayMode.UNION);

        // Adding Markers
        Marker marker = scatter.marker(dataForScatter);
        marker.type(MarkerType.CIRCLE)
                .size(4d);
        marker.tooltip()
                .hAlign(HAlign.START)
                .format("Title: ${%title} \\nDate: ${%X} \\nAmount: ${%Value}");

        // FIXME: 21-10-2020 Adding different mappings


        // fixme Adding zoom to Scatter Chart


        chartPlaceholder.setChart(scatter);
    }

    private static class CustomScatterData extends ValueDataEntry {
        CustomScatterData(Integer day, Double amount, String categoryColor, String title) {
            super(day, amount);
            setValue("fill", categoryColor);
            setValue("title", title);
        }
    }
}