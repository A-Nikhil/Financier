package com.a_nikhil.financier.commons;

import android.text.Html;
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

    private ArrayList<String> mExpenditureTitles, mExpenditureCategories, mExpenditureDates, mExpenditureAmounts;

    public RecyclerViewAdapter(ArrayList<String> mExpenditureTitles, ArrayList<String> mExpenditureCategories,
                               ArrayList<String> mExpenditureDates, ArrayList<String> mExpenditureAmounts) {
        this.mExpenditureTitles = mExpenditureTitles;
        this.mExpenditureCategories = mExpenditureCategories;
        this.mExpenditureDates = mExpenditureDates;
        this.mExpenditureAmounts = mExpenditureAmounts;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenditure_item, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.expenditureTitle.setText(mExpenditureTitles.get(position));
        holder.expenditureAmount.setText(Html.fromHtml(
                "\u20B9 " + mExpenditureAmounts.get(position), Html.FROM_HTML_MODE_COMPACT));
        holder.expenditureCategory.setText(mExpenditureCategories.get(position));
        holder.expenditureDate.setText(mExpenditureDates.get(position));

            /*
            // CHECKPOINT: Put Actions here, if any
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView expenditureTitle, expenditureCategory, expenditureDate, expenditureAmount;
        ConstraintLayout expenditureItemLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureItemLayout = itemView.findViewById(R.id.expenditureItemView);
            expenditureTitle = itemView.findViewById(R.id.expenditureTitle);
            expenditureAmount = itemView.findViewById(R.id.expenditureAmount);
            expenditureCategory = itemView.findViewById(R.id.expenditureCategory);
            expenditureDate = itemView.findViewById(R.id.expenditureDate);
        }
    }
}
