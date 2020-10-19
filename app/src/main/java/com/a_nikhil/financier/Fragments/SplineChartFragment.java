package com.a_nikhil.financier.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Expenditure;

import java.util.ArrayList;


public class SplineChartFragment extends Fragment {
    private static final String TAG = "SplineChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_spline_chart, container, false);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
//        final double maxIncome = inputBundle.containsKey("maxIncome") ? inputBundle.getDouble("maxIncome") : 0d;
        final double maxIncome = 30000d;
        Log.d(TAG, "onCreateView: " + expenditures.toString());
        Log.d(TAG, "onCreateView: Max Income = " + maxIncome);
        return rootView;
    }

    private void createSplineChart(ArrayList<Expenditure> expenditures) {

    }
}