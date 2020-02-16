package com.a_nikhil.financier.Fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mExpenditureTitles = new ArrayList<>(),
            mExpenditureCategories = new ArrayList<>(),
            mExpenditureDates = new ArrayList<>(),
            mExpenditureAmounts = new ArrayList<>();

    public RecyclerViewAdapter(Context mContext,
                               ArrayList<String> mExpenditureTitles, ArrayList<String> mExpenditureCategories,
                               ArrayList<String> mExpenditureDates, ArrayList<String> mExpenditureAmounts) {
        this.mContext = mContext;
        this.mExpenditureTitles = mExpenditureTitles;
        this.mExpenditureCategories = mExpenditureCategories;
        this.mExpenditureDates = mExpenditureDates;
        this.mExpenditureAmounts = mExpenditureAmounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenditure_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.expenditureTitle.setText(mExpenditureTitles.get(position));
        holder.expenditureAmount.setText(mExpenditureAmounts.get(position));
        holder.expenditureCategory.setText(mExpenditureCategories.get(position));
        holder.expenditureDate.setText(mExpenditureDates.get(position));

        /*
        // LOGIC HINT: Put Actions here, if any
        holder.expenditureItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked an item");
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mExpenditureTitles.size();
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
