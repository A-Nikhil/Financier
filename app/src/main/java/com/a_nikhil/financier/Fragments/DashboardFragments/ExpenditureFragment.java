package com.a_nikhil.financier.Fragments.DashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.NewExpenditureActivity;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.AndroidUtilities.ShowStatusAsSnackbar;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;
import com.a_nikhil.financier.commons.RecyclerViewAdapterExpenditures;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpenditureFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private final ArrayList<String> mExpenditureTitles = new ArrayList<>(),
            mExpenditureCategories = new ArrayList<>(),
            mExpenditureDates = new ArrayList<>(),
            mExpenditureAmounts = new ArrayList<>();
    private String userEmail;
    private DatabaseHelper db;
    private View rootView;
    private String username;
    private String maxIncome;
    private ShowStatusAsSnackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_expenditure, container, false);

        assert getActivity() != null;
        getActivity().setTitle("Expenditures");
        Log.d(TAG, "onCreateView: called");

        db = new DatabaseHelper(getActivity().getApplicationContext());

        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;
        userEmail = inputBundle.getString("email");
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        username = db.getUserData().getName();
        maxIncome = db.getUserData().getMaxIncome().toString();
        Toast.makeText(getActivity(), userEmail, Toast.LENGTH_SHORT).show();

        snackbar = new ShowStatusAsSnackbar(getActivity().getApplicationContext(),
                getActivity().findViewById(R.id.fragment_container));

        /*
        Bundle from Dashboard
        Case 1: Initial Click => Does not contains newExpenditurePresent
        Case 2: Coming from NewExpenditureActivity =>
                        Case 2.1 => NewExpenditureAdded = true ; contains newExpenditureData
                        Case 2.2 => NewExpenditureAdded = false
         */

        if (!inputBundle.containsKey("newExpenditurePresent")) {
            addDataToList(false, false);
        } else {
            if (inputBundle.getBoolean("newExpenditurePresent")) {
                // expenditureData[] = {name, amount, date, category};
                String[] expenditureData = inputBundle.getStringArray("newExpenditureData");
                Log.d(TAG, "onCreateView: Expenditure data: " + Arrays.asList(expenditureData));
                assert expenditureData != null;
                setNewExpenditureFromActivity(expenditureData[0], expenditureData[1],
                        expenditureData[2], expenditureData[3]);
            } else {
                addDataToList(false, true);
            }
        }

        (rootView.findViewById(R.id.floatingActionButton)).setOnClickListener(view -> {
            //  Opens up a dialog to add new Expenditure
            addNewExpenditure();
        });
        return rootView;
    }

    public void setNewExpenditureFromActivity(String name, String amount, String date, String category) {
        String TAG = "ReceivedInput";
        Log.d(TAG, "Name = " + name);
        Log.d(TAG, "Amount = " + amount);
        Log.d(TAG, "Date = " + date);
        Log.d(TAG, "Category = " + category);
        Expenditure expenditure = new Expenditure(name, Double.parseDouble(amount),
                date, Category.valueOf(category.toUpperCase()));

        String collection = getResources().getString(R.string.collection);

        // Add input to database
        assert getActivity() != null;
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        db.insertExpenditure(expenditure);

        // Add to firebase
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(collection).document(userEmail)
                .update("expenditures", FieldValue.arrayUnion(expenditure));

        addDataToList(true, true);
    }

    private void addDataToList(boolean updateList, boolean addingActivityVisited) {

        if (updateList) {
            mExpenditureTitles.clear();
            mExpenditureAmounts.clear();
            mExpenditureDates.clear();
            mExpenditureCategories.clear();
        }
        ArrayList<Expenditure> expenditures = db.getExpenditureDataAsList();
        for (Expenditure expenditure : expenditures) {
            mExpenditureTitles.add(expenditure.getTitle());
            mExpenditureAmounts.add(expenditure.getAmount().toString());
            mExpenditureDates.add(expenditure.getDate());
            mExpenditureCategories.add(expenditure.getCategory().getDescription());
        }
        initRecyclerView();

        // Show status on snackbar
        assert getActivity() != null;
        snackbar.showStatus(addingActivityVisited ? (updateList ? "New Expenditure Added" : "Action Cancelled")
                : "Expenditures loaded");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: initiator called");
        RecyclerView view = rootView.findViewById(R.id.recyclerViewExpenditure);
        RecyclerViewAdapterExpenditures adapter = new RecyclerViewAdapterExpenditures(
                getActivity(),
                mExpenditureTitles,
                mExpenditureCategories,
                mExpenditureDates,
                mExpenditureAmounts,
                username, maxIncome);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    private void addNewExpenditure() {
        Bundle emailBundle = new Bundle();
        emailBundle.putString("email", userEmail);
        Intent getNewExpenditure = new Intent(getActivity(), NewExpenditureActivity.class);
        getNewExpenditure.putExtras(emailBundle);
        assert getActivity() != null;
        getActivity().startActivity(getNewExpenditure);
    }
}

