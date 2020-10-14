package com.a_nikhil.financier;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class VisualizationHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "VisualizationHomePage";

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_home_page);

        drawer = findViewById(R.id.visualization_drawer_layout);
        NavigationView vizNav = findViewById(R.id.dash_vis_nav);
        View vizHeader = vizNav.getHeaderView(0);

        // Get Bundle
        Bundle inputBundle = getIntent().getExtras();
        assert inputBundle != null;

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}