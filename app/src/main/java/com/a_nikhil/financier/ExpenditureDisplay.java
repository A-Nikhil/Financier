package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ExpenditureDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_display);
        setTitle("Expense Details");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);

        int flag = Html.FROM_HTML_MODE_COMPACT;
        String username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String maxIncomeString = Objects.requireNonNull(getIntent().getExtras()).getString("maxIncome");
        double maxIncome = Double.parseDouble(maxIncomeString);
        String title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        String amountString = Objects.requireNonNull(getIntent().getExtras()).getString("amount");
        double amount = Double.parseDouble(amountString);
        String date = Objects.requireNonNull(getIntent().getExtras()).getString("date");
        String category = Objects.requireNonNull(getIntent().getExtras()).getString("category");

        ((TextView) findViewById(R.id.singleExpenditureName)).setText(Html.fromHtml(
                "Hello, <b>" + username + "</b>", flag));
        ((TextView) findViewById(R.id.singleExpenditureTitle)).setText(Html.fromHtml(
                "You spent <b> \u20B9 " + amount + "</b><br/>" +
                        "on <b>" + title + "</b>", flag));
        ((TextView) findViewById(R.id.singleExpenditureCategory)).setText(Html.fromHtml(
                "This comes under the category<br/>of <b>" + category + "</b>", flag));
        ((TextView) findViewById(R.id.singleExpenditureDate)).setText(Html.fromHtml(
                "on <b>" + date + "</b>", flag));
        ((ProgressBar) findViewById(R.id.singleExpenditureBar)).setProgress((int) (amount * 100 / maxIncome), true);
        ((TextView) findViewById(R.id.singleExpenditurePercentage)).setText(Html.fromHtml(
                "<b>" + (int) (amount * 100 / maxIncome) + " %</b>", flag));
        ((TextView) findViewById(R.id.singleExpenditureTotalIncome)).setText(Html.fromHtml(
                "of your total monthly income of \u20B9 " + maxIncome, flag));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(ExpenditureDisplay.this, Dashboard.class));
        return super.onOptionsItemSelected(item);
    }
}
