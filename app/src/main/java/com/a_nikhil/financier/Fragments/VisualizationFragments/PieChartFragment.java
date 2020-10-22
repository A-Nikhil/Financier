package com.a_nikhil.financier.Fragments.VisualizationFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.CategoryColorMap;
import com.a_nikhil.financier.commons.Expenditure;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PieChartFragment extends Fragment {

    private static final String TAG = "PieChartFragment";

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        context = getActivity();

        Log.d(TAG, "onCreateView: Enter PieChart");

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
        Double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        if (expenditures == null) {
            Snackbar.make(rootView, "No Expenditure", Snackbar.LENGTH_SHORT).show();
            return rootView;
        }
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);
        displayPieChart(rootView, expenditures, maxIncome);
        return rootView;
    }

    private void displayPieChart(final View rootView, ArrayList<Expenditure> expenditures, Double maxIncome) {
        AnyChartView pieView = rootView.findViewById(R.id.pie_chart_space);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(context, event.getData().get("x") + " : " +
                                event.getData().get("value")
                        , Toast.LENGTH_SHORT).show();
            }
        });


        // Add Data to the chart
        List<DataEntry> dataForPie = new ArrayList<>();
        String[] piePalette = formatDataForPie(expenditures, maxIncome, dataForPie);
        pie.data(dataForPie);

        // Add other details
        pie.title("Category-wise distribution");

        pie.labels().position("outside");

        pie.legend().title().text("Categories").padding(0d, 0d, 10d, 0d);

        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        pie.palette(piePalette);

        Log.d(TAG, "displayPieChart: DataEntry : " + dataForPie);
        Log.d(TAG, "displayPieChart: Palette : " + Arrays.asList(piePalette));

        pieView.setChart(pie);
    }

    private String[] formatDataForPie(ArrayList<Expenditure> expenditures, double maxIncome, List<DataEntry> dataForPie) {
        double[] categoryWise = new double[8];
        double sum = 0d;
        for (Expenditure expenditure : expenditures) {
            Log.d(TAG, "formatDataForPie: " + expenditure);
            Category category = expenditure.getCategory();
            double amount = expenditure.getAmount();
            categoryWise[category.getIndex() - 1] += amount;
            sum += amount;
        }

        // Fetching category color data
        CategoryColorMap colorDetails = new CategoryColorMap();
        HashMap<Category, String> colorMap = colorDetails.getColorMap();
        String[] categories = colorDetails.getCategories();

        List<String> paletteGen = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (categoryWise[i] > 0) {
                dataForPie.add(new ValueDataEntry(categories[i], categoryWise[i]));
                paletteGen.add(colorMap.get(Category.valueOf(categories[i])));
            }
        }

        // Money Left
        Log.d(TAG, "formatDataForPie: Sum = " + sum + " maxIncome = " + maxIncome);
        if (sum < maxIncome) {
            Log.d(TAG, "formatDataForPie: Adding savings");
            dataForPie.add(new ValueDataEntry("Saved", maxIncome - sum));
            paletteGen.add("#424949");
        }

        // Generate palette
        return paletteGen.toArray(new String[0]);
    }
}

