package com.a_nikhil.financier;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Update Button
        (findViewById(R.id.updateIncomeButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txt = findViewById(R.id.updateIncomeAmount);
                String amount = txt.getText().toString();
                // FIXME: 17-02-2020 Update Firebase
            }
        });

        // Wipe Data
        (findViewById(R.id.wipeDataCleanButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new DatabaseHelper(getApplicationContext()).wipeClean()) {
                    Log.d(TAG, "onClick: wipe clean works");
                } else {
                    Log.d(TAG, "onClick: wipe clean sucked");
                }
            }
        });

        // Add Dark Mode Button
    }
}
