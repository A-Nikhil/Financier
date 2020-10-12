package com.a_nikhil.financier.commons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.ExpenditureDisplay;
import com.a_nikhil.financier.R;

import java.util.ArrayList;

public class RecyclerViewAdapterExpenditures extends RecyclerView.Adapter<RecyclerViewAdapterExpenditures.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterExpenditures";
    private ArrayList<String> mExpenditureTitles, mExpenditureCategories, mExpenditureDates, mExpenditureAmounts;
    private String username;
    private Context mContext;
    private String maxIncome;

    public RecyclerViewAdapterExpenditures(Context mContext,
                                           ArrayList<String> mExpenditureTitles,
                                           ArrayList<String> mExpenditureCategories,
                                           ArrayList<String> mExpenditureDates,
                                           ArrayList<String> mExpenditureAmounts,
                                           String username,
                                           String maxIncome) {
        this.mContext = mContext;
        this.mExpenditureTitles = mExpenditureTitles;
        this.mExpenditureCategories = mExpenditureCategories;
        this.mExpenditureDates = mExpenditureDates;
        this.mExpenditureAmounts = mExpenditureAmounts;
        this.username = username;
        this.maxIncome = maxIncome;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterExpenditures.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenditure_item, parent, false);
        return new RecyclerViewAdapterExpenditures.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterExpenditures.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.expenditureTitle.setText(mExpenditureTitles.get(position));
        holder.expenditureAmount.setText(Html.fromHtml(
                "\u20B9 " + mExpenditureAmounts.get(position), Html.FROM_HTML_MODE_COMPACT));
        holder.expenditureCategory.setText(mExpenditureCategories.get(position));
        holder.expenditureDate.setText(mExpenditureDates.get(position));

        //  Put Actions here, if any
        holder.expenditureItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked an item");
                Intent intent = new Intent(mContext, ExpenditureDisplay.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("title", mExpenditureTitles.get(position));
                myBundle.putString("amount", mExpenditureAmounts.get(position));
                myBundle.putString("date", mExpenditureDates.get(position));
                myBundle.putString("category", mExpenditureCategories.get(position));
                myBundle.putString("username", username);
                myBundle.putString("maxIncome", maxIncome);
                intent.putExtras(myBundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpenditureTitles.size();
    }

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
