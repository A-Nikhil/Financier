package com.a_nikhil.financier.Fragments.DashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.a_nikhil.financier.R;
import com.a_nikhil.financier.VisualizationHomePage;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

public class VisualizeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualize, container, false);

        FragmentActivity activity = getActivity();
        assert activity != null;

        DatabaseHelper db = new DatabaseHelper(activity);
        Bundle inputBundle = this.getArguments();
        assert inputBundle != null;

        String email = inputBundle.getString("email");
        String name = db.getUserData().getName();
        String maxIncome = db.getUserData().getMaxIncome().toString();

        int numberOfExpenditures = db.getExpenditureDataAsList().size();

        final Bundle outputBundle = new Bundle();
        outputBundle.putString("email", email);
        outputBundle.putString("name", name);
        outputBundle.putString("maxIncome", maxIncome);

        (rootView.findViewById(R.id.visualize_button_fragment)).setOnClickListener(view -> {
            if (numberOfExpenditures == 0) {
                Snackbar.make(getActivity().findViewById(R.id.fragment_container),
                        "No Expenditures Yet", Snackbar.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), VisualizationHomePage.class);
                intent.putExtras(outputBundle);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
