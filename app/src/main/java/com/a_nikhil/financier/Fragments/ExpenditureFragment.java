package com.a_nikhil.financier.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.R;

import java.util.ArrayList;
import java.util.Objects;

public class ExpenditureFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private ArrayList<String> mExpenditureTitles = new ArrayList<>(),
            mExpenditureCategories = new ArrayList<>(),
            mExpenditureDates = new ArrayList<>(),
            mExpenditureAmounts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        addDataToList();
        return inflater.inflate(R.layout.fragment_new_expenditure, container, false);
    }

    private void addDataToList() {
        mExpenditureTitles.add("Est raptus urbs, cesaris.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Secundus fluctuss ducunt ad calcaria.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Emeritis fortis virtualiter captiss secula est.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Stella de dexter bubo, transferre vita!");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Barcas cadunts, tanquam audax xiphias.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Accola clemens mensa est.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Nunquam examinare equiso.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Heu.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Musa, calcaria, et fortis.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        mExpenditureTitles.add("Mens cresceres, tanquam fortis solem.");
        mExpenditureCategories.add("food");
        mExpenditureDates.add("12th September 2019");
        mExpenditureAmounts.add("12000");

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initiator called");
        RecyclerView view = Objects.requireNonNull(getView()).findViewById(R.id.recyclerViewExpenditure);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),
                mExpenditureTitles, mExpenditureCategories,
                mExpenditureDates, mExpenditureAmounts);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(getView().getContext()));
    }
}
