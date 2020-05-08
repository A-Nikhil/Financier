package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(About.this, Dashboard.class));
        return super.onOptionsItemSelected(item);
    }

    public void clickToSeeRepo(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/A-Nikhil/myIMDB")));
    }
}
