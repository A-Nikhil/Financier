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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenditureFragment extends Fragment implements NewExpenditureDialog.OnItemInsert {

    @Override
    public void sendInput(String name, String amount, String date, String category) {
        String TAG = "ReceivedInput";
        Log.d(TAG, "Name = " + name);
        Log.d(TAG, "Amount = " + amount);
        Log.d(TAG, "Date = " + date);
        Log.d(TAG, "Category = " + category);
        // FIXME: 17-02-2020 Add input to database
    }

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

    private void addDataToList(final View rootView) {

        // FIXME: 16-02-2020 ADD Data from firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserExpenditure(db, "alanT@enigma.com", new ExpenditureCallback() {
            @Override
            public void onCallback(HashMap<String, HashMap<String, String>> expenditures) {
                for (Map.Entry<String, HashMap<String, String>> entry1 : expenditures.entrySet()) {
                    for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {
                        String key = entry2.getKey();
                        switch (key) {
                            case "title":
                                mExpenditureTitles.add(entry2.getValue());
                                break;
                            case "category":
                                mExpenditureCategories.add(entry2.getValue());
                                break;
                            case "amount":
                                mExpenditureAmounts.add(entry2.getValue());
                                break;
                            default:
                                mExpenditureDates.add(entry2.getValue());
                                break;
                        }
                    }
                }
                initRecyclerView(rootView);
            }
        });
    }

    private void getUserExpenditure(FirebaseFirestore db, String email, final ExpenditureCallback callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, HashMap<String, String>> expenditures = new HashMap<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.get("expenditures").getClass().toString());
                        }
                        callback.onCallback(expenditures);
                    }
                });
    }

    private interface ExpenditureCallback {
        void onCallback(HashMap<String, HashMap<String, String>> expenditures);
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
        expenditureDialog.setTargetFragment(ExpenditureFragment.this, 1);
        expenditureDialog.show(getActivity().getSupportFragmentManager(), "add new expenditure");
    }
}

