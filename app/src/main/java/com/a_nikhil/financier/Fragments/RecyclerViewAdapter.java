package com.a_nikhil.financier.Fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private static final String TAG = "RecyclerViewAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView expenditureTitle, expenditureCategory, expenditureDate, expenditureAmount;
        ConstraintLayout expenditureItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureItemLayout = itemView.findViewById(R.id.expenditureItemView);
            expenditureTitle = itemView.findViewById(R.id.expenditureTitle);
            expenditureAmount = itemView.findViewById(R.id.expenditureAmount);
            expenditureCategory = itemView.findViewById(R.id.expenditureCategory);
            expenditureDate = itemView.findViewById(R.id.expenditureDate);
        }
    }
}
