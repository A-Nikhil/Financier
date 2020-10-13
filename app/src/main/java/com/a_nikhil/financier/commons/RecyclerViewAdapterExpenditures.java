package com.a_nikhil.financier.commons;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.a_nikhil.financier.ExpenditureDisplay;
import com.a_nikhil.financier.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

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

        // Title
        holder.expenditureTitle.setText(mExpenditureTitles.get(position));

        // Amount
        holder.expenditureAmount.setText(Html.fromHtml(
                "\u20B9 " + mExpenditureAmounts.get(position), Html.FROM_HTML_MODE_COMPACT));

        // Date
        holder.expenditureDate.setText(mExpenditureDates.get(position));

        // Category chip
        String category = mExpenditureCategories.get(position);
        ColorStateList categoryColor = null;
        Drawable categoryIcon = null;
        switch (category.toLowerCase()) {
            case "food":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_food);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_food_black_48dp);
                break;
            case "household":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_household);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_household_black_48dp);
                break;
            case "social":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_social);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_social_black_48dp);
                break;
            case "work":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_work);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_work_black_48dp);
                break;
            case "amenities":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_amenities);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_amenities_black_48dp);
                break;
            case "recreation":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_recreation);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_recreation_black_48dp);
                break;
            case "travel":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_travel);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_travel_black_48dp);
                break;
            case "education":
                categoryColor = ContextCompat.getColorStateList(mContext, R.color.chip_education);
                categoryIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_education_black_48dp);
                break;
        }
        holder.expenditureCategory.setText(mExpenditureCategories.get(position));
        holder.expenditureCategory.setChipBackgroundColor(categoryColor);
        holder.expenditureCategory.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        holder.expenditureCategory.setChipIcon(categoryIcon);

        // Opening an expenditure in detail
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

        // Deleting an expenditure
        holder.deleteExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Deleting", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpenditureTitles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView expenditureTitle, expenditureDate, expenditureAmount;
        ConstraintLayout expenditureItemLayout;
        Chip expenditureCategory;
        ImageButton deleteExpenditure;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureItemLayout = itemView.findViewById(R.id.expenditureItemView);
            expenditureTitle = itemView.findViewById(R.id.expenditureTitle);
            expenditureAmount = itemView.findViewById(R.id.expenditureAmount);
            expenditureCategory = itemView.findViewById(R.id.recycler_item_category_chip);
            expenditureDate = itemView.findViewById(R.id.expenditureDate);
            deleteExpenditure = itemView.findViewById(R.id.recycler_item_delete_button);
        }
    }
}
