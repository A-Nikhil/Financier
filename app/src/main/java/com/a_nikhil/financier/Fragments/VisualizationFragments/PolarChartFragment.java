package com.a_nikhil.financier.Fragments.VisualizationFragments;

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
import com.anychart.charts.Polar;
import com.anychart.enums.PolarSeriesType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.ScaleTypes;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.scales.Linear;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PolarChartFragment extends Fragment {
    private static final String TAG = "PolarChartFragment";
    private View rootView;
    private ArrayList<Expenditure> expenditures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_polar_chart, container, false);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        expenditures = new DummyExpenditures().getExpenditureDataAsList();
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }

        Log.d(TAG, "onCreateView: " + expenditures.toString());


        createPolarChart();

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    private void createPolarChart() {
        AnyChartView anyChartView = rootView.findViewById(R.id.polar_chart_view);
        anyChartView.setProgressBar(rootView.findViewById(R.id.polar_progress_bar));
        Polar polar = AnyChart.polar();

        // Create day of week map
        Map<String, Double> dayOfWeekMap = new LinkedHashMap<>();
        dayOfWeekMap.put("Monday", 0d);
        dayOfWeekMap.put("Tuesday", 0d);
        dayOfWeekMap.put("Wednesday", 0d);
        dayOfWeekMap.put("Thursday", 0d);
        dayOfWeekMap.put("Friday", 0d);
        dayOfWeekMap.put("Saturday", 0d);
        dayOfWeekMap.put("Sunday", 0d);

        // Get Date
        LocalDate date;
        for (Expenditure expenditure : expenditures) {
            String[] eDate = expenditure.getDate().split("/");
            date = LocalDate.of(Integer.parseInt(eDate[2]),
                    Integer.parseInt(eDate[1]),
                    Integer.parseInt(eDate[0]));
            String dayNameKey = date.getDayOfWeek().name();
            dayNameKey = dayNameKey.substring(0, 1).toUpperCase() + dayNameKey.substring(1).toLowerCase(); // CamelCase
            dayOfWeekMap.put(dayNameKey, dayOfWeekMap.get(dayNameKey) + expenditure.getAmount());
        }

        List<DataEntry> dataForPolar = new ArrayList<>();
        for (Map.Entry<String, Double> entry : dayOfWeekMap.entrySet()) {
            dataForPolar.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }

        polar.column(dataForPolar);

        polar.title("Expenditure on Days of Week");

        polar.sortPointsByX(true)
                .defaultSeriesType(PolarSeriesType.COLUMN)
                .yAxis(false)
                .xScale(ScaleTypes.ORDINAL);

        polar.title().margin().bottom(20d);

        ((Linear) polar.yScale(Linear.class)).stackMode(ScaleStackMode.VALUE);

        polar.tooltip()
                .valuePrefix("$")
                .displayMode(TooltipDisplayMode.UNION);

        anyChartView.setChart(polar);
    }
}