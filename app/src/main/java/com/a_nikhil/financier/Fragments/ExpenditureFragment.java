package com.a_nikhil.financier.Fragments;

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

import com.a_nikhil.financier.DialogActivity.NewExpenditureDialog;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;
import com.a_nikhil.financier.commons.RecyclerViewAdapterExpenditures;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ExpenditureFragment extends Fragment implements NewExpenditureDialog.OnItemInsert {

    @Override
    public void sendInput(String name, String amount, String date, String category) {
        String TAG = "ReceivedInput";
        Log.d(TAG, "Name = " + name);
        Log.d(TAG, "Amount = " + amount);
        Log.d(TAG, "Date = " + date);
        Log.d(TAG, "Category = " + category);
        Expenditure expenditure = new Expenditure(name, Double.parseDouble(amount),
                date, Category.valueOf(category.toUpperCase()));

        // FIXME: 17-02-2020 Add input to database
        DatabaseHelper db = new DatabaseHelper(getActivity());
        db.insertExpenditure(expenditure);

        // FIXME: 22-02-2020 Add to firebase
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(userFirestoreId)
                .update("expenditures", FieldValue.arrayUnion(expenditure));

        addDataToList(true);
    }

    private String userFirestoreId;
    private DatabaseHelper db;
    private static final String TAG = "DashboardFragment";
    private ArrayList<String> mExpenditureTitles = new ArrayList<>(),
            mExpenditureCategories = new ArrayList<>(),
            mExpenditureDates = new ArrayList<>(),
            mExpenditureAmounts = new ArrayList<>();
    private View rootView;
    private String username;
    private String maxIncome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_expenditure, container, false);
        Log.d(TAG, "onCreateView: called");
        db = new DatabaseHelper(getActivity());
//        assert this.getArguments() != null;
//        userFirestoreId = this.getArguments().getString("userFirestoreId");
//        String username = getArguments().getString("username");
//        String maxIncome = getArguments().getString("maxIncome");
        userFirestoreId = "zi16pAymAnxAF8u5C2Bu";
        username = "Alan Turing";
        maxIncome = "100000";
        Toast.makeText(getActivity(), userFirestoreId, Toast.LENGTH_SHORT).show();

        addDataToList(false);

        (rootView.findViewById(R.id.floatingActionButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CHECKPOINT: Opens up a dialog to add new Expenditure
                addNewExpenditure();
            }
        });
        return rootView;
    }

    private void addDataToList(boolean updateList) {

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
        NewExpenditureDialog expenditureDialog = new NewExpenditureDialog(getContext());
        expenditureDialog.setTargetFragment(ExpenditureFragment.this, 1);
        expenditureDialog.show(getActivity().getSupportFragmentManager(), "add new expenditure");
    }
}

