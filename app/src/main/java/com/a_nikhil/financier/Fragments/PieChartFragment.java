package com.a_nikhil.financier.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.Expenditure;

import java.util.ArrayList;

public class PieChartFragment extends Fragment {

    private static final String TAG = "PieChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        Log.d(TAG, "onCreateView: Enter PieChart");

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        ArrayList<Expenditure> expenditures = inputBundle.getParcelableArrayList("expenditures");
        Log.d(TAG, "onCreateView: " + expenditures.toString());

        return myView;
    }
}