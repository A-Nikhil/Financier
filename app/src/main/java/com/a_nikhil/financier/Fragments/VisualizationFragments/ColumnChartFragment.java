package com.a_nikhil.financier.Fragments.VisualizationFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.CategoryColorMap;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnChartFragment extends Fragment {
    private static final String TAG = "ColumnChartFragment";
    private boolean graphAdjustedToIncome = false;
    private Cartesian columns;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;
        final Button adjustButton = rootView.findViewById(R.id.adjust_graph);

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
        final double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);

        // InitData
        final List<DataEntry> dataForColumn = new ArrayList<>();
        formColumnDataAndPalette(expenditures, dataForColumn);

        // Construct Chart
        constructColumnChart(rootView, dataForColumn);
        adjustButton.setClickable(true);

        // Graph will be adjusted for maximum income
        adjustButton.setOnClickListener(view -> {
            graphAdjustedToIncome = !graphAdjustedToIncome;
            columns.yScale().maximum(maxIncome);
        });

        return rootView;
    }


    private void constructColumnChart(final View rootView, List<DataEntry> dataForColumn) {
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.column_chart_placeholder);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.progress_bar_column));

        columns = AnyChart.column();
        Column series = columns.column(dataForColumn);
        series.name("Expenditure");

//        columns.data(dataForColumn);

        columns.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        columns.animation(true);

        columns.yScale().minimum(0d);

        columns.xScroller(true);
        columns.xZoom().setToPointsCount(3, false, columns.xScale());

        columns.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        columns.tooltip().positionMode(TooltipPositionMode.POINT);
        columns.interactivity().hoverMode(HoverMode.BY_X);

        columns.xAxis(0).title("Category");
        columns.yAxis(0).title("Amount");

        chartPlaceholder.setChart(columns);
    }

    private void formColumnDataAndPalette(ArrayList<Expenditure> expenditures, List<DataEntry> dataForColumn) {
        double[] categoryWise = new double[8];
        for (Expenditure expenditure : expenditures) {
            Log.d(TAG, "formatDataForPie: " + expenditure);
            Category category = expenditure.getCategory();
            double amount = expenditure.getAmount();
            categoryWise[category.getIndex() - 1] += amount;
        }

        // Fetching category color data
        CategoryColorMap colorDetails = new CategoryColorMap();
        HashMap<Category, String> colorMap = colorDetails.getColorMap();
        String[] categories = colorDetails.getCategories();

        for (int i = 0; i < 8; i++) {
            if (categoryWise[i] > 0) {
                String color = colorMap.get(Category.valueOf(categories[i]));
                Log.d(TAG, "formColumnDataAndPalette: {Category=" + categories[i] + ",Color=" + color);
                dataForColumn.add(new ColumnDataEntry(categories[i], categoryWise[i], color));
            }
        }
    }

    private static class ColumnDataEntry extends ValueDataEntry {
        ColumnDataEntry(String x, Number value, String color) {
            super(x, value);
            setValue("fill", color);
            setValue("stroke", 0);
        }
    }
}