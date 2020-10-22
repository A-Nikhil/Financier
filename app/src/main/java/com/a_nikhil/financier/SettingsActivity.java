package com.a_nikhil.financier;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private String collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        collection = getResources().getString(R.string.collection);

        // Update Button
        (findViewById(R.id.updateIncomeButton)).setOnClickListener(view -> {
            EditText txt = findViewById(R.id.updateIncomeAmount);
            double amount = Double.parseDouble(txt.getText().toString());

            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            User user = db.getUserData();
            user.setMaxIncome(amount);
            db.updateMaxIncome(amount, user.getEmail());

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection(collection).document(user.getEmail())
                    .update("maxIncome", amount);

            Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
        });

        // Wipe Data
        (findViewById(R.id.wipeDataCleanButton)).setOnClickListener(view -> {
            if (new DatabaseHelper(getApplicationContext()).wipeClean()) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG, "onClick: wipe clean works");
            } else {
                Log.d(TAG, "onClick: wipe clean sucked");
            }
        });

        // Email Verification
        (findViewById(R.id.emailVerifiedButton)).setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                if (user.isEmailVerified()) {
                    Toast.makeText(SettingsActivity.this, "Email Already Verified", Toast.LENGTH_SHORT).show();
                } else {
                    user.sendEmailVerification().addOnSuccessListener(aVoid ->
                            Toast.makeText(SettingsActivity.this, "Email sent", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
