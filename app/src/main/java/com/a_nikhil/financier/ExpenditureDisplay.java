package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.text.HAlign;

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
        assert maxIncomeString != null;
        double maxIncome = Double.parseDouble(maxIncomeString);
        String title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        String amountString = Objects.requireNonNull(getIntent().getExtras()).getString("amount");
        assert amountString != null;
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

        // Visualization starting here
        AnyChartView anyChartView = findViewById(R.id.anyChartView);
        anyChartView.setChart(setSpeedometer(maxIncome, amount));

        // fixme changes
//        ((ProgressBar) findViewById(R.id.singleExpenditureBar)).setProgress((int) (amount * 100 / maxIncome), true);
//        ((TextView) findViewById(R.id.singleExpenditurePercentage)).setText(Html.fromHtml(
//                "<b>" + (int) (amount * 100 / maxIncome) + " %</b>", flag));
//        ((TextView) findViewById(R.id.singleExpenditureTotalIncome)).setText(Html.fromHtml(
//                "of your total monthly income of \u20B9 " + maxIncome, flag));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(ExpenditureDisplay.this, Dashboard.class));
        return super.onOptionsItemSelected(item);
    }

    private CircularGauge setSpeedometer(double maxIncome, double amount) {
        CircularGauge circularGauge = AnyChart.circular();

        // Making the gauge
        circularGauge
                .stroke(null)
                .startAngle(0)
                .sweepAngle(360);

        // Setting data
        double percentage = amount * 100 / maxIncome;
        percentage = (double) Math.round(percentage * 100) / 100;
        circularGauge.data(new SingleValueDataSet(new Double[]{percentage}));

        // Axes
        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{type:'line', length:4, position:'outside'}");

        circularGauge.axis(0).labels().position("outside");

        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(100);

        circularGauge.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 10}");

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("80%")
                .startWidth("2%")
                .endWidth(0);

        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: 25\">" + amount + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + percentage + "% </span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(1)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 20,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'green 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.range(1,
                "{\n" +
                        "    from: 80,\n" +
                        "    to: 100,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");


        return circularGauge;
    }
}
