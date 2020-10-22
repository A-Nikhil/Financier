package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.a_nikhil.financier.Fragments.VisualizationFragments.CircularGaugeFragment;
import com.a_nikhil.financier.Fragments.VisualizationFragments.ColumnChartFragment;
import com.a_nikhil.financier.Fragments.VisualizationFragments.PieChartFragment;
import com.a_nikhil.financier.Fragments.VisualizationFragments.PolarChartFragment;
import com.a_nikhil.financier.Fragments.VisualizationFragments.ScatterChartFragment;
import com.a_nikhil.financier.Fragments.VisualizationFragments.SplineChartFragment;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.Expenditure;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class VisualizationHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "VisualizationHomePage";

    private DrawerLayout drawer;
    private Bundle outputBundle;
    private String email;
    private NavigationView vizNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_home_page);

        Toolbar toolbar = findViewById(R.id.vis_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.visualization_drawer_layout);
        vizNav = findViewById(R.id.dash_vis_nav);
        View vizHeader = vizNav.getHeaderView(0);
        vizNav.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.dashboard_nav_drawer_open,
                R.string.dashboard_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get Bundle
        Bundle inputBundle = getIntent().getExtras();
        assert inputBundle != null;

        /* Uncomment when testing is done
        Bundle inputBundle = new Bundle();
        inputBundle.putString("name", "Covid");
        inputBundle.putString("maxIncome", "25000");
        inputBundle.putString("email", "covid19@pandemic.org");
         */

        // Get name from Bundle
        String name = inputBundle.getString("name");
        String maxIncome = inputBundle.getString("maxIncome");
        email = inputBundle.getString("email");
        Log.d(TAG, "onCreate: name = " + name);
        Log.d(TAG, "onCreate: max income = " + maxIncome);
        Log.d(TAG, "onCreate: email = " + email);

        // Setting name and max income
        ((TextView) vizHeader.findViewById(R.id.nav_header_name)).setText(name);
        int flag = Html.FROM_HTML_MODE_COMPACT;
        ((TextView) vizHeader.findViewById(R.id.nav_header_email)).setText(Html.fromHtml(
                "\u20B9 " + maxIncome, flag));

        // Get data from local database
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//        DummyExpenditures db = new DummyExpenditures(); // Get dummy expenditures for testing
        ArrayList<Expenditure> expenditures = db.getExpenditureDataAsList();

        // Generate an output bundle
        outputBundle = new Bundle();
        outputBundle.putParcelableArrayList("expenditures", expenditures);
        outputBundle.putDouble("maxIncome", maxIncome == null ? 0 : Double.parseDouble(maxIncome));

        // Call on Pie chart
        PieChartFragment pieChartFragment = new PieChartFragment();
        pieChartFragment.setArguments(outputBundle);
        vizNav.setCheckedItem(R.id.yearly_percentage_pie_chart);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viz_fragment_container, pieChartFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (item.getItemId() == R.id.yearly_percentage_pie_chart) {
            PieChartFragment pieChartFragment = new PieChartFragment();
            pieChartFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.yearly_percentage_pie_chart);
            transaction.replace(R.id.viz_fragment_container, pieChartFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.yearly_percentage_column_chart) {
            ColumnChartFragment columnChartFragment = new ColumnChartFragment();
            columnChartFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.yearly_percentage_column_chart);
            transaction.replace(R.id.viz_fragment_container, columnChartFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.spline_chart_overall) {
            SplineChartFragment splineChartFragment = new SplineChartFragment();
            splineChartFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.spline_chart_overall);
            transaction.replace(R.id.viz_fragment_container, splineChartFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.scatter_chart) {
            ScatterChartFragment scatterChartFragment = new ScatterChartFragment();
            scatterChartFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.scatter_chart);
            transaction.replace(R.id.viz_fragment_container, scatterChartFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.circular_gauge) {
            CircularGaugeFragment circularGaugeFragment = new CircularGaugeFragment();
            circularGaugeFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.circular_gauge);
            transaction.replace(R.id.viz_fragment_container, circularGaugeFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.polar_chart) {
            PolarChartFragment polarChartFragment = new PolarChartFragment();
            polarChartFragment.setArguments(outputBundle);
            vizNav.setCheckedItem(R.id.polar_chart);
            transaction.replace(R.id.viz_fragment_container, polarChartFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.exit_to_dashboard) {
            outputBundle.clear();
            outputBundle.putString("email", email);
            Intent exitToDashboard = new Intent(VisualizationHomePage.this, Dashboard.class);
            exitToDashboard.putExtras(outputBundle);
            startActivity(exitToDashboard);
        } else {
            Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}