package com.a_nikhil.financier.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Objects.requireNonNull(getActivity()).setTitle("Your Dashboard");
        Log.d(TAG, "onCreateView: Dashboard Fragment Called");
        assert this.getArguments() != null;
        String userFirestoreId = this.getArguments().getString("firestoreId");
        Log.d("posty", "DashFrag : " + userFirestoreId);
//        String userFirestoreId = "zi16pAymAnxAF8u5C2Bu";

        // Progress Bar
        setDashboard(myView, userFirestoreId, getActivity());
        return myView;
    }

    private void setDashboard(final View rootView, final String userFirestoreId, final Activity activity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress_circle);
        final AlertDialog dialog = builder.create();
        dialog.show();
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

                addStatsToDatabase(user, expendituresList, rootView);

                /* Uncomment this for testing only => Add import com.a_nikhil.financier.TestingModules; at the top
                // Testing modules
                TestingModules testingModules = new TestingModules();
                testingModules.checkUser(db);
                testingModules.checkExpenditure(db);
                */
                dialog.dismiss();
            }
        });
    }

    // CHECKPOINT: Calculating stats and adding to Database
    private void addStatsToDatabase(User user, ArrayList<Expenditure> expenditureList, View rootView) {

        int flag = Html.FROM_HTML_MODE_COMPACT;

        // Setting Name
        ((TextView) rootView.findViewById(R.id.helloUser)).setText(Html.fromHtml(
                "Hello, <b>" + user.getName() + "</b>",
                flag));

        ((TextView) rootView.findViewById(R.id.financierText)).setText(R.string.financierText);
        ((TextView) rootView.findViewById(R.id.financierText)).setTextSize(14);
        ((TextView) rootView.findViewById(R.id.topStats)).setText(R.string.topStats);

        TextView numberExpenditures = rootView.findViewById(R.id.numberExpenditures);
        TextView totalMonthly = rootView.findViewById(R.id.totalMonthly);
        TextView monthlyPercentage = rootView.findViewById(R.id.monthlyPercentage);
        TextView topCategory = rootView.findViewById(R.id.topCategory);
        TextView secondCategory = rootView.findViewById(R.id.secondCategory);
        TextView statsTotal = rootView.findViewById(R.id.statsTotal);
        TextView remark = rootView.findViewById(R.id.remark);

        if (expenditureList.size() == 0) {
            numberExpenditures.setText(Html.fromHtml("You have <b>not added</b> any expenditures yet", flag));
            totalMonthly.setText(Html.fromHtml("", flag));
            monthlyPercentage.setText(Html.fromHtml("", flag));
            topCategory.setText(Html.fromHtml("", flag));
            secondCategory.setText(Html.fromHtml("", flag));
            statsTotal.setText(Html.fromHtml("", flag));
            remark.setText(Html.fromHtml("", flag));
        } else {
            numberExpenditures.setText(Html.fromHtml("You have added <b>" + expenditureList.size() + "</b> expenditures", flag));

            // getting complete data
            DataHandler myHandler = new DataHandler(expenditureList, user.getMaxIncome());
            myHandler.getData();
            totalMonthly.setText(Html.fromHtml(
                    "You have spent <b>\u20B9 " + myHandler.monthlySum + "</b>" +
                            " from your total income of <b>\u20B9 " + user.getMaxIncome() + "</b>", flag));
            monthlyPercentage.setText(Html.fromHtml(
                    "These expenditures account for <b>" + myHandler.monthlyPercentage + "% </b>" +
                            "of your monthly income", flag));
            topCategory.setText(Html.fromHtml(
                    "You have spent most of your money on " +
                            "<b>" + myHandler.topCategory.getDescription() + "</b>, a total of <b>\u20B9 " + myHandler.topCategorySum + "</b>", flag));
            secondCategory.setText(Html.fromHtml(
                    "Next being <b>" + myHandler.secondCategory.getDescription() + "</b> with a total expenditure of " +
                            "<b>\u20B9 " + myHandler.secondCategorySum + "</b>", flag));
            statsTotal.setText(Html.fromHtml(
                    "Till date, you have spent a total of " +
                            "<b>\u20B9 " + myHandler.totalStatsNumber + "</b> i.e <b>" + myHandler.totalStatsPercentage + "%</b> of your total earnings", flag));
            String remarks;
            if (myHandler.totalStatsPercentage < 60 && myHandler.monthlyPercentage < 60) {
                remarks = "Going good on overall";
            } else if (myHandler.monthlyPercentage < 60) {
                remarks = "Monthly looking strong";
            } else if (myHandler.totalStatsPercentage < 60) {
                remarks = "Overall looking strong";
            } else {
                remarks = "Going bad";
            }
            remark.setText(remarks);
        }
    }

    private class DataHandler {
        private ArrayList<Expenditure> expenditureList;
        private double maxIncome;
        Category topCategory;
        double topCategorySum;
        Category secondCategory;
        double secondCategorySum;
        int monthlyPercentage;
        double monthlySum;
        double totalStatsNumber;
        int totalStatsPercentage;

        DataHandler(ArrayList<Expenditure> expenditureList, double maxIncome) {
            this.expenditureList = expenditureList;
            this.maxIncome = maxIncome;
        }

        void getData() {
            // Category Array
            String[] myCategories = new String[]{"FOOD", "RENT", "MORTGAGE", "HOUSEHOLD", "CASUAL", "WORK", "OUTDOORS",
                    "RECREATION", "TRAVEL", "STATIONARY", "EDUCATION"};

            // expense array
            double[] expenses = new double[11];

            // getting monthly percentage
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            int month = Integer.parseInt(formatter.format(date).split("/")[1]);
            double totalMonthlyExpense = 0.0;

            // getting number of months active
            HashSet<Integer> monthsActive = new HashSet<>();
            double totalSum = 0.0;

            for (Expenditure expenditure : this.expenditureList) {
                expenses[expenditure.getCategory().getIndex() - 1] += expenditure.getAmount();

                // getting monthly expense
                if (Integer.parseInt(expenditure.getDate().split("/")[1]) == month) {
                    totalMonthlyExpense += expenditure.getAmount();
                }

                // getting active months
                monthsActive.add(Integer.parseInt(expenditure.getDate().split("/")[1]));
                totalSum += expenditure.getAmount();
            }

            // getting top category
            double first = 0.0;
            double second = 0.0;
            int firstPos = 0, secondPos = 0;
            for (int i = 0; i < 11; i++) {
                if (expenses[i] > first) {
                    second = first;
                    first = expenses[i];
                    firstPos = i;
                } else if (expenses[i] > second && expenses[i] != first) {
                    second = expenses[i];
                    secondPos = i;
                }
            }


            this.topCategory = Category.valueOf(myCategories[firstPos]);
            this.secondCategory = Category.valueOf(myCategories[secondPos]);
            this.topCategorySum = first;
            this.secondCategorySum = second;

            // getting monthly percentage
            this.monthlySum = totalMonthlyExpense;
            this.monthlyPercentage = (int) Math.round(totalMonthlyExpense * 100.0d / maxIncome);

            // getting mega total
            this.totalStatsNumber = totalSum;
            this.totalStatsPercentage = (int) Math.round(totalSum * 100.0d / (maxIncome * monthsActive.size()));
        }
    }

    // CHECKPOINT: Getting Data from FirebaseFirestore
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