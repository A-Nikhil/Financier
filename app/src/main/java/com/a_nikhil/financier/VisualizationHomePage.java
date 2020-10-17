package com.a_nikhil.financier;

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

import com.a_nikhil.financier.Fragments.ColumnChartFragment;
import com.a_nikhil.financier.Fragments.PieChartFragment;
import com.a_nikhil.financier.commons.AndroidUtilities;
import com.a_nikhil.financier.commons.Expenditure;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class VisualizationHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "VisualizationHomePage";

    private DrawerLayout drawer;
    private Bundle outputBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_home_page);

        Toolbar toolbar = findViewById(R.id.vis_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.visualization_drawer_layout);
        NavigationView vizNav = findViewById(R.id.dash_vis_nav);
        View vizHeader = vizNav.getHeaderView(0);
        vizNav.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.dashboard_nav_drawer_open,
                R.string.dashboard_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*
        // Get Bundle
        Bundle inputBundle = getIntent().getExtras();
        assert inputBundle != null;
         */

//        /* Uncomment when testing is done
        Bundle inputBundle = new Bundle();
        inputBundle.putString("name", "Covid");
        inputBundle.putString("maxIncome", "25000");
        inputBundle.putString("email", "covid19@pandemic.org");
//         */

        // Get name from Bundle
        String name = inputBundle.getString("name");
        String maxIncome = inputBundle.getString("maxIncome");
        String email = inputBundle.getString("email");
        Log.d(TAG, "onCreate: name = " + name);
        Log.d(TAG, "onCreate: max income = " + maxIncome);
        Log.d(TAG, "onCreate: email = " + email);

        // Setting name and max income
        ((TextView) vizHeader.findViewById(R.id.nav_header_name)).setText(name);
        int flag = Html.FROM_HTML_MODE_COMPACT;
        ((TextView) vizHeader.findViewById(R.id.nav_header_email)).setText(Html.fromHtml(
                "\u20B9 " + maxIncome, flag));

        /*
        // Loading circle
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress_circle);
        final AlertDialog dialog = builder.create();
        dialog.show();
        */

        // Get data from local database
        // DatabaseHelper db = new DatabaseHelper(this);
        DummyExpenditures db = new DummyExpenditures(); // Get dummy expenditures for testing
        ArrayList<Expenditure> expenditures = db.getExpenditureDataAsList();

        // Generate an output bundle
        outputBundle = new Bundle();
        outputBundle.putParcelableArrayList("expenditures", expenditures);
        outputBundle.putDouble("maxIncome", maxIncome == null ? 0 : Double.parseDouble(maxIncome));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AndroidUtilities.ShowStatusAsSnackbar snackbar = new AndroidUtilities.ShowStatusAsSnackbar(getApplicationContext(), drawer);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.monthly_percentage_pie_chart:
                Toast.makeText(this, "PieChart called", Toast.LENGTH_SHORT).show();
                PieChartFragment pieChartFragment = new PieChartFragment();
                pieChartFragment.setArguments(outputBundle);
                transaction.replace(R.id.viz_fragment_container, pieChartFragment)
                        .addToBackStack(null).commit();
                break;
            case R.id.monthly_percentage_column_chart:
                Toast.makeText(this, "ColumnChart called", Toast.LENGTH_SHORT).show();
                ColumnChartFragment columnChartFragment = new ColumnChartFragment();
                columnChartFragment.setArguments(outputBundle);
                transaction.replace(R.id.viz_fragment_container, columnChartFragment)
                        .addToBackStack(null).commit();
                break;
            case R.id.categories_vs_total2:
                snackbar.showStatus("C");
                break;
            case R.id.categories_vs_total3:
                snackbar.showStatus("D");
                break;
            default:
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