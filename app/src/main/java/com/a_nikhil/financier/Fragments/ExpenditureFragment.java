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

import com.a_nikhil.financier.DialogActivity.NewExpenditureDialog;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.commons.RecyclerViewAdapter;

import java.util.ArrayList;

public class ExpenditureFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private ArrayList<String> mExpenditureTitles = new ArrayList<>(),
            mExpenditureCategories = new ArrayList<>(),
            mExpenditureDates = new ArrayList<>(),
            mExpenditureAmounts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_expenditure, container, false);
        Log.d(TAG, "onCreateView: called");
        addDataToList(myView);

        (myView.findViewById(R.id.floatingActionButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpenditure();
            }
        });
        return myView;
    }

    private void addDataToList(View rootView) {

        // FIXME: 16-02-2020 ADD Data from firebase

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

        initRecyclerView(rootView);
    }

    private void initRecyclerView(View rootView) {
        Log.d(TAG, "initRecyclerView: initiator called");
        RecyclerView view = rootView.findViewById(R.id.recyclerViewExpenditure);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),
                mExpenditureTitles, mExpenditureCategories,
                mExpenditureDates, mExpenditureAmounts);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    private void addNewExpenditure() {
        NewExpenditureDialog expenditureDialog = new NewExpenditureDialog(getContext());
        expenditureDialog.show(getActivity().getSupportFragmentManager(), "add new expenditure");
    }
}

