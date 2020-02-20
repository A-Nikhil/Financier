package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.ConnectionStatus;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Registration");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    public void clickRegistration(View v) {

        // CHECKPOINT: Checking Internet Connection
        if (new ConnectionStatus().isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = ((EditText) findViewById(R.id.username)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPassword)).getText().toString();
        String maxIncome = ((EditText) findViewById(R.id.signupMonthlyIncome)).getText().toString();
        boolean termsAreChecked = ((CheckBox) findViewById(R.id.TermsAndConditions)).isChecked();

        // Validation
        try {
            if (name.length() == 0) {
                throw new Exception("Name cannot be empty");
            } else if (!name.matches("^[A-Za-z ]*$")) {
                throw new Exception("Name can only contain Alphabets and Spaces");
            } else if (email.length() == 0) {
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw new Exception("Please enter a valid email address");
            } else if (phone.length() == 0) {
                throw new Exception("Phone cannot be empty");
            } else if (!Patterns.PHONE.matcher(phone).matches()) {
                throw new Exception("Invalid phone number");
            } else if (password.length() == 0) {
                throw new Exception("Password cannot be empty");
            } else if (!password.matches("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})")) {
                throw new Exception("Password must contain 1 uppercase, 1 lowercase and 1 digit");
            } else if (!password.equals(confirmPassword)) {
                throw new Exception("Passwords don't match");
            } else if (maxIncome.length() == 0) {
                throw new Exception("Income cannot be zero");
            } else if (!termsAreChecked) {
                throw new Exception("Please agree to terms and conditions");
            }
            registerNewUser(new User(name, email, phone, password,
                    Double.parseDouble(maxIncome)));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void registerNewUser(final User user) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String collection = "users";
        final DatabaseHelper localDB = new DatabaseHelper(getApplicationContext());
        db.collection(collection)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                        // CHECKPOINT: Add ID to user object
                        user.setFirestoreID(documentReference.getId());

                        // CHECKPOINT: Add Firestore ID to user
                        db.collection("users").document(documentReference.getId())
                                .update("firestoreID", documentReference.getId(),
                                        "expenditures", Collections.emptyList());

                        // CHECKPOINT: Send to cache (local db)
                        addToCache(user, localDB);

                        // CHECKPOINT: Send intent to dashboard
                        startActivity(new Intent(SignupActivity.this, Dashboard.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // CHECKPOINT: Adding the current signed in user to local db
    private void addToCache(User user, DatabaseHelper db) {
        if (db.insertUser(user)) {
            Log.d("Signup Activity", "Added to cache");
        }
    }

}
