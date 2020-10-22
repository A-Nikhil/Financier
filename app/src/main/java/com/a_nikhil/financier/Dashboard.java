package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.a_nikhil.financier.DialogActivity.LogoutDialog;
import com.a_nikhil.financier.Fragments.DashboardFragments.DashboardFragment;
import com.a_nikhil.financier.Fragments.DashboardFragments.ExpenditureFragment;
import com.a_nikhil.financier.Fragments.DashboardFragments.StatsFragment;
import com.a_nikhil.financier.Fragments.DashboardFragments.VisualizeFragment;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.User;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final Bundle myBundle = new Bundle();
    private DrawerLayout drawer;
    private String email;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle inputBundle = getIntent().getExtras();
        assert inputBundle != null;
        email = inputBundle.containsKey("email") ? inputBundle.getString("email") : "No Email found";

        /* Uncomment for testing
        email = "covid19@pandemic.org";
        inputBundle.putBoolean("coming_from_login_signup", true);
        Test section over */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.dashboard_drawer_layout);
        navigationView = findViewById(R.id.dash_nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // Set Name and Email
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = db.getUserData();
        TextView navName = (headerView.findViewById(R.id.nav_header_name));
        navName.setText(user.getName());
        TextView navEmail = (headerView.findViewById(R.id.nav_header_email));
        navEmail.setText(user.getEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.dashboard_nav_drawer_open,
                R.string.dashboard_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        myBundle.putString("email", email);
        if (inputBundle.containsKey("coming_from_login_signup")) {
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.setArguments(myBundle);
            myBundle.clear();
            myBundle.putString("email", email);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    dashboardFragment).commit();
            navigationView.setCheckedItem(R.id.menu_dashboard);
        } else {
            if (inputBundle.containsKey("newExpenditurePresent")) {
                boolean newExpenditurePresent = inputBundle.getBoolean("newExpenditurePresent");
                Log.d("NewExpenditureActivity test", "onCreate: New Expenditure Present = " + newExpenditurePresent);
                if (newExpenditurePresent) {
                    String[] expenditureDetails = inputBundle.getStringArray("newExpenditureData");
                    myBundle.putStringArray("newExpenditureData", expenditureDetails);
                }
                myBundle.putBoolean("newExpenditurePresent", newExpenditurePresent);
                ExpenditureFragment expenditureFragment = new ExpenditureFragment();
                expenditureFragment.setArguments(myBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, expenditureFragment).commit();
                navigationView.setCheckedItem(R.id.menu_expenditure);
            } else {
                DashboardFragment dashboardFragment = new DashboardFragment();
                dashboardFragment.setArguments(myBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        dashboardFragment).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (menuItem.getItemId() == R.id.menu_dashboard) {          // Dashboard
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.setArguments(myBundle);
            myBundle.clear();
            myBundle.putString("email", email);
            navigationView.setCheckedItem(R.id.menu_dashboard);
            transaction.replace(R.id.fragment_container,
                    dashboardFragment).addToBackStack(null).commit();

        } else if (menuItem.getItemId() == R.id.menu_expenditure) {          // Expenditure
            ExpenditureFragment expenditureFragment = new ExpenditureFragment();
            expenditureFragment.setArguments(myBundle);
            myBundle.clear();
            myBundle.putString("email", email);
            navigationView.setCheckedItem(R.id.menu_expenditure);
            transaction.replace(R.id.fragment_container,
                    expenditureFragment).addToBackStack(null).commit();
        } else if (menuItem.getItemId() == R.id.menu_visualize) {          // Visualize
            VisualizeFragment visualizeFragment = new VisualizeFragment();
            visualizeFragment.setArguments(myBundle);
            myBundle.clear();
            myBundle.putString("email", email);
            navigationView.setCheckedItem(R.id.menu_visualize);
            transaction.replace(R.id.fragment_container,
                    visualizeFragment).addToBackStack(null).commit();
        } else if (menuItem.getItemId() == R.id.menu_stats) {          // Dashboard
            StatsFragment statsFragment = new StatsFragment();
            statsFragment.setArguments(myBundle);
            myBundle.clear();
            myBundle.putString("email", email);
            transaction.replace(R.id.fragment_container,
                    statsFragment).addToBackStack(null).commit();
        } else if (menuItem.getItemId() == R.id.cloud_download) {
            Toast.makeText(getApplicationContext(), "Downloaded from cloud", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.cloud_upload) {
            Toast.makeText(getApplicationContext(), "Uploaded to cloud", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_dot_menu) {
            startActivity(new Intent(Dashboard.this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.about_dot_menu) {
            startActivity(new Intent(Dashboard.this, About.class));
            return true;
        }
        if (item.getItemId() == R.id.logout_dot_menu) {
            openDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        LogoutDialog logoutDialog = new LogoutDialog(db);
        logoutDialog.show(getSupportFragmentManager(), "logout alert");
    }
}
