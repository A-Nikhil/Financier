package com.a_nikhil.financier.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Log.d(TAG, "onCreateView: Dashboard Fragment Called");
//        assert this.getArguments() != null;
//        String userFirestoreId = this.getArguments().getString("userFirestoreId");
        String userFirestoreId = "zi16pAymAnxAF8u5C2Bu";
        setDashboard(myView, userFirestoreId);
        return myView;
    }

    private void setDashboard(final View rootView, final String userFirestoreId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserDetails(db, userFirestoreId, new DashboardCallback() {
            @Override
            public void onCallback(HashMap<String, Object> expenditures) {
                User user = new User();
                ArrayList<Expenditure> expendituresList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : expenditures.entrySet()) {
                    String key = entry.getKey().toLowerCase();
                    switch (key) {
                        case "expenditures":
                            // CHECKPOINT: entry.getValue() returns ArrayList<HashMap<String, Object>>
                            @SuppressWarnings("unchecked")
                            ArrayList<HashMap<String, Object>> mapper1 =
                                    (ArrayList<HashMap<String, Object>>) entry.getValue();
                            for (HashMap<String, Object> mapper2 : mapper1) {
                                Expenditure expenditure = new Expenditure();
                                for (Map.Entry<String, Object> mapper3 : mapper2.entrySet()) {
                                    String insideKey = mapper3.getKey().toLowerCase();
                                    switch (insideKey) {
                                        case "amount":
                                            expenditure.setAmount(Double.parseDouble(mapper3.getValue().toString()));
                                            break;
                                        case "category":
                                            expenditure.setCategory(Category.valueOf(mapper3.getValue().toString().toUpperCase()));
                                            break;
                                        case "date":
                                            expenditure.setDate(mapper3.getValue().toString());
                                            break;
                                        case "title":
                                            expenditure.setTitle(mapper3.getValue().toString());
                                            break;
                                    }
                                }
                                expendituresList.add(expenditure);
                            }
                            break;
                        case "name":
                            user.setName(entry.getValue().toString());
                            break;
                        case "phone":
                            user.setPhone(entry.getValue().toString());
                            break;
                        case "password":
                            user.setPassword(entry.getValue().toString());
                            break;
                        case "firestoreid":
                            user.setFirestoreID(entry.getValue().toString());
                            break;
                        case "email":
                            user.setEmail(entry.getValue().toString());
                            break;
                        case "maxincome":
                            user.setMaxIncome(Double.parseDouble(entry.getValue().toString()));
                            break;
                    }
                }

                // CHECKPOINT: Adding the above data to local DB
                DatabaseHelper db = new DatabaseHelper(getActivity());
                if (db.wipeClean()) {
                    Log.d(TAG, "onCallback: Wipe Data worked");
                } else {
                    Log.d(TAG, "onCallback: Wipe Data sucked");
                }
                db = new DatabaseHelper(getActivity());
                int total = expendituresList.size(); // TESTING
                int count = 0;
                for (Expenditure expenditure : expendituresList) {
                    if (db.insertExpenditure(expenditure)) {
                        count++;
                    }
                }
                Log.i(TAG, "onCallback: Number of expenditures added : " + count + " out of " + total);
                db.insertUser(user);

//                TestingModules testingModules = new TestingModules();
//                testingModules.checkUser(db);
//                testingModules.checkExpenditure(db);
            }
        });
    }

    private void getUserDetails(FirebaseFirestore db, String userFirestoreId, final DashboardCallback callback) {
        db.collection("users").document(userFirestoreId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        HashMap<String, Object> expenditures = new HashMap<>();
                        if ((task.getResult() != null) && (task.getResult().getData() != null)) {
                            for (Map.Entry<String, Object> taskEntry : task.getResult().getData().entrySet()) {
                                expenditures.put(taskEntry.getKey(), taskEntry.getValue());
                            }
                            callback.onCallback(expenditures);
                        }
                    }
                });
    }

    private interface DashboardCallback {
        void onCallback(HashMap<String, Object> expenditures);
    }
}
