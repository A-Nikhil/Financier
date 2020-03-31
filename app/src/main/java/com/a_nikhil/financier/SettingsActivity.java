package com.a_nikhil.financier;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.User;
import com.google.firebase.firestore.FirebaseFirestore;

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
                double amount = Double.parseDouble(txt.getText().toString());

                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                User user = db.getUserData();
                user.setMaxIncome(amount);
                db.updateMaxIncome(amount, user.getEmail());

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("users").document(user.getEmail())
                        .update("maxIncome", amount);

                Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
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
