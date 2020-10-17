package com.a_nikhil.financier.Fragments;

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
import com.anychart.charts.Cartesian;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnChartFragment extends Fragment {
    private static final String TAG = "ColumnChartFragment";
    private boolean graphAdjustedToIncome = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
        final double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);

        // InitData
        final List<DataEntry> dataForColumn = new ArrayList<>();
        final String[] palette = formColumnDataAndPalette(expenditures, dataForColumn);

        // Construct Chart
        constructColumnChart(rootView, dataForColumn, palette, 0d);

        // Graph will be adjusted for maximum income
        (rootView.findViewById(R.id.adjust_graph)).setOnClickListener(view -> {
            graphAdjustedToIncome = !graphAdjustedToIncome;
            constructColumnChart(rootView, dataForColumn, palette,
                    graphAdjustedToIncome ? 0d : maxIncome);
        });

        return rootView;
    }

    private void constructColumnChart(final View rootView, List<DataEntry> dataForColumn, String[] palette,
                                      double maxIncome) {
        AnyChartView chartPlaceholder = rootView.findViewById(R.id.column_chart_placeholder);
        chartPlaceholder.setProgressBar(rootView.findViewById(R.id.progress_bar_column));

        Cartesian columns = AnyChart.column();

        columns.data(dataForColumn);

        columns.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        columns.animation(true);

        columns.yScale().minimum(0d);
        if (maxIncome != 0d) {
            columns.yScale().maximum(maxIncome);
        }

        columns.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        columns.tooltip().positionMode(TooltipPositionMode.POINT);
        columns.interactivity().hoverMode(HoverMode.BY_X);

        columns.xAxis(0).title("Category");
        columns.yAxis(0).title("Amount");

        columns.palette(palette);

        chartPlaceholder.setChart(columns);
    }

    private String[] formColumnDataAndPalette(ArrayList<Expenditure> expenditures, List<DataEntry> dataForColumn) {
        ArrayList<String> palette = new ArrayList<>();
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
                dataForColumn.add(new ValueDataEntry(categories[i], categoryWise[i]));
                palette.add(colorMap.get(Category.valueOf(categories[i])));
            }
        }
        return palette.toArray(new String[0]);
    }
}