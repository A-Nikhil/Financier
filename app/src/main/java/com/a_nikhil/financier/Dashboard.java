package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
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

import com.a_nikhil.financier.DialogActivity.LogoutDialog;
import com.a_nikhil.financier.Fragments.DashboardFragment;
import com.a_nikhil.financier.Fragments.ExpenditureFragment;
import com.a_nikhil.financier.Fragments.PredictFragment;
import com.a_nikhil.financier.Fragments.VisualizeFragment;
import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.User;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.dashboard_drawer_layout);
        NavigationView navigationView = findViewById(R.id.dash_nav_view);
        View headerView = navigationView.getHeaderView(0);

        // Set Name and Email
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = db.getUserData();
        TextView navName = (headerView.findViewById(R.id.nav_header_name));
        navName.setText(user.getName());
        TextView navEmail = (headerView.findViewById(R.id.nav_header_email));
        navEmail.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.dashboard_nav_drawer_open,
                R.string.dashboard_nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new DashboardFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ExpenditureFragment()).commit();
        }
        navigationView.setCheckedItem(R.id.menu_dashboard);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                break;
            case R.id.menu_add_new:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ExpenditureFragment()).commit();
                break;
            case R.id.menu_visualize:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VisualizeFragment()).commit();
                break;
            case R.id.menu_predict:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PredictFragment()).commit();
                break;
            case R.id.cloud_download:
                Toast.makeText(getApplicationContext(), "Downloaded from cloud", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cloud_upload:
                Toast.makeText(getApplicationContext(), "Uploaded to cloud", Toast.LENGTH_SHORT).show();
                break;
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
        switch (item.getItemId()) {
            case R.id.settings_dot_menu:
                startActivity(new Intent(Dashboard.this, SettingsActivity.class));
                return true;
            case R.id.about_dot_menu:
                startActivity(new Intent(Dashboard.this, About.class));
                return true;
            case R.id.logout_dot_menu:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void openDialog() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        LogoutDialog logoutDialog = new LogoutDialog(db);
        logoutDialog.show(getSupportFragmentManager(), "logout alert");
    }
}
