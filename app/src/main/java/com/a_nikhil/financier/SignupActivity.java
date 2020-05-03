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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.ConnectionStatus;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private String collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Registration");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        collection = getString(R.string.collection);
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

        final EditText fieldName = findViewById(R.id.username);
        final EditText fieldPhone = findViewById(R.id.phone);
        final EditText fieldPassword = findViewById(R.id.password);
        final EditText fieldConfirmPassword = findViewById(R.id.confirmPassword);
        final EditText fieldMaxIncome = findViewById(R.id.signupMonthlyIncome);
        final boolean termsAreChecked = ((CheckBox) findViewById(R.id.TermsAndConditions)).isChecked();

        final String name = fieldName.getText().toString();
        final String phone = fieldPhone.getText().toString();
        final String password = fieldPassword.getText().toString();
        final String confirmPassword = fieldConfirmPassword.getText().toString();
        final String maxIncome = fieldMaxIncome.getText().toString();

        final EditText emailText = findViewById(R.id.email);
        final String email = emailText.getText().toString();

        if (!validateCredentials(name, email, phone, password,
                confirmPassword, maxIncome,
                fieldName, fieldPhone, emailText,
                fieldPassword, fieldConfirmPassword, fieldMaxIncome,
                termsAreChecked)) {
            return;
        }

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        getEmailData(db, email, new TestingCallback() {
            @Override
            public void testingCallback(boolean exists) {
                if (exists) {
                    Toast.makeText(getApplicationContext(), "Callback : Email exists", Toast.LENGTH_SHORT).show();
                    // make edit text red
                    DrawableCompat.setTint(emailText.getBackground(),
                            ContextCompat.getColor(getApplicationContext(), R.color.duplicateEmail));
                    emailText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.duplicateEmail));
                } else {

                    // FIXME: 01-04-2020 Add Signup here
                    User user = new User(name, email, phone, password,
                            Double.parseDouble(maxIncome));
                    registerNewUser(db, user);
                }
            }
        });

    }

    public void registerNewUser(final FirebaseFirestore db, final User user) {
        addFirebaseUser(user.getEmail(), user.getPassword());
        final DatabaseHelper localDB = new DatabaseHelper(getApplicationContext());
        db.collection(collection)
                .document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                        // CHECKPOINT: Send to cache (local db)
                        addToCache(user, localDB);

                        // CHECKPOINT: Send intent to dashboard
                        Intent intent = new Intent(SignupActivity.this, Dashboard.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putString("email", user.getEmail());
                        intent.putExtras(myBundle);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // CHECKPOINT: Adding Firebase Authentication
    private void addFirebaseUser(String email, String password) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password);
    }

    // CHECKPOINT: Adding the current signed in user to local db
    private void addToCache(User user, DatabaseHelper db) {
        if (db.insertUser(user)) {
            Log.d("Signup Activity", "Added to cache");
        }
    }

    private interface TestingCallback {
        void testingCallback(boolean exists);
    }

    private void getEmailData(FirebaseFirestore db, String givenEmail, final TestingCallback callback) {
        // FIXME: 01-04-2020 Change collection to users
        db.collection("testCollection").document(givenEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        boolean exists = false;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                exists = true;
                            }
                            callback.testingCallback(exists);
                        }
                    }
                });
    }

    private boolean validateCredentials(String name, String email, String phone, String password,
                                        String confirmPassword, String maxIncome,
                                        EditText fieldName, EditText fieldPhone, EditText fieldEmail,
                                        EditText fieldPassword, EditText fieldConfirmPassword,
                                        EditText fieldMaxIncome, boolean termsAreChecked) {

        removeTintFromEditText(fieldName);
        removeTintFromEditText(fieldEmail);
        removeTintFromEditText(fieldPhone);
        removeTintFromEditText(fieldPassword);
        removeTintFromEditText(fieldConfirmPassword);
        removeTintFromEditText(fieldMaxIncome);

        try {
            if (name.length() == 0) {
                setTintOnEditText(fieldName);
                throw new Exception("Name cannot be empty");
            } else if (!name.matches("^[A-Za-z ]*$")) {
                setTintOnEditText(fieldName);
                throw new Exception("Name can only contain Alphabets and Spaces");
            } else if (email.length() == 0) {
                setTintOnEditText(fieldEmail);
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setTintOnEditText(fieldEmail);
                throw new Exception("Please enter a valid email address");
            } else if (phone.length() == 0) {
                setTintOnEditText(fieldPhone);
                throw new Exception("Phone cannot be empty");
            } else if (!Patterns.PHONE.matcher(phone).matches()) {
                setTintOnEditText(fieldPhone);
                throw new Exception("Invalid phone number");
            } else if (password.length() == 0) {
                setTintOnEditText(fieldPassword);
                throw new Exception("Password cannot be empty");
            } else if (!password.matches("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})")) {
                setTintOnEditText(fieldPassword);
                throw new Exception("Password must contain 1 uppercase, 1 lowercase and 1 digit");
            } else if (!password.equals(confirmPassword)) {
                setTintOnEditText(fieldConfirmPassword);
                throw new Exception("Passwords don't match");
            } else if (maxIncome.length() == 0) {
                setTintOnEditText(fieldMaxIncome);
                throw new Exception("Income cannot be zero");
            } else if (!termsAreChecked) {
                throw new Exception("Please agree to terms and conditions");
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setTintOnEditText(EditText textField) {
        DrawableCompat.setTint(textField.getBackground(),
                ContextCompat.getColor(getApplicationContext(), R.color.duplicateEmail));

        // setting Text Color
        textField.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.duplicateEmail));
    }

    private void removeTintFromEditText(EditText textField) {
        DrawableCompat.setTint(textField.getBackground(),
                ContextCompat.getColor(getApplicationContext(), R.color.accent));

        // setting Text Color
        textField.setTextColor(getColor(R.color.black));
    }

}
