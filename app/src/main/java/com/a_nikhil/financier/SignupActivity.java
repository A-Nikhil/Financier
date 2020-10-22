package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.AndroidUtilities;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private String collection;
    private Context context;
    private AndroidUtilities.AddAndRemoveTints tints;
    private AndroidUtilities.ShowStatusAsSnackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Registration");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        collection = getString(R.string.collection);
        tints = new AndroidUtilities.AddAndRemoveTints();
        context = getApplicationContext();
        snackbar = new AndroidUtilities.ShowStatusAsSnackbar(context, findViewById(R.id.signup_screen));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void clickRegistration(View v) {

        // hide soft keyboard
        hideSoftKeyboard();

        //  Checking Internet Connection
        if (new AndroidUtilities.ConnectionStatus().isNetworkConnected(getApplicationContext())) {
            snackbar.showStatus("No Internet Connection");
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
        User user = new User(name, email, phone, password,
                Double.parseDouble(maxIncome));
        addFirebaseUser(email, password, user, db, emailText);

    }

    public void registerNewUser(final FirebaseFirestore db, final User user) {

        // setting User Basic Details
        FirebaseUser FbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (FbUser != null) {
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getName())
                    .build();
            FbUser.updateProfile(request);
        }

        final DatabaseHelper localDB = new DatabaseHelper(getApplicationContext());
        db.collection(collection)
                .document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                        //  Send to cache (local db)
                        addToCache(user, localDB);

                        //  Send intent to dashboard
                        Intent goToDashboard = new Intent(SignupActivity.this, Dashboard.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putString("email", user.getEmail());
//                        myBundle.putString("name", user.getName());
//                        myBundle.putString("maxIncome", String.valueOf(user.getMaxIncome()));
                        myBundle.putBoolean("coming_from_login_signup", true);
                        goToDashboard.putExtras(myBundle);
                        startActivity(goToDashboard);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar.showStatus("Not Registered");
                    }
                });
    }

    //  Adding Firebase Authentication
    private void addFirebaseUser(String email, String password,
                                 final User user, final FirebaseFirestore db,
                                 final EditText emailText) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignupActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        registerNewUser(db, user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar.showStatus("Email Already exists");
                        tints.setTintOnEditText(context, emailText);
                    }
                });
    }

    //  Adding the current signed in user to local db
    private void addToCache(User user, DatabaseHelper db) {
        if (db.insertUser(user)) {
            Log.d("Signup Activity", "Added to cache");
        }
    }

    private boolean validateCredentials(String name, String email, String phone, String password,
                                        String confirmPassword, String maxIncome,
                                        EditText fieldName, EditText fieldPhone, EditText fieldEmail,
                                        EditText fieldPassword, EditText fieldConfirmPassword,
                                        EditText fieldMaxIncome, boolean termsAreChecked) {

        tints.removeTintFromEditText(context, fieldName);
        tints.removeTintFromEditText(context, fieldEmail);
        tints.removeTintFromEditText(context, fieldPhone);
        tints.removeTintFromEditText(context, fieldPassword);
        tints.removeTintFromEditText(context, fieldConfirmPassword);
        tints.removeTintFromEditText(context, fieldMaxIncome);

        try {
            if (name.length() == 0) {
                tints.setTintOnEditText(context, fieldName);
                throw new Exception("Name cannot be empty");
            } else if (!name.matches("^[A-Za-z ]*$")) {
                tints.setTintOnEditText(context, fieldName);
                throw new Exception("Name can only contain Alphabets and Spaces");
            } else if (email.length() == 0) {
                tints.setTintOnEditText(context, fieldEmail);
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tints.setTintOnEditText(context, fieldEmail);
                throw new Exception("Please enter a valid email address");
            } else if (phone.length() == 0) {
                tints.setTintOnEditText(context, fieldPhone);
                throw new Exception("Phone cannot be empty");
            } else if (!Patterns.PHONE.matcher(phone).matches()) {
                tints.setTintOnEditText(context, fieldPhone);
                throw new Exception("Invalid phone number");
            } else if (password.length() == 0) {
                tints.setTintOnEditText(context, fieldPassword);
                throw new Exception("Password cannot be empty");
            } else if (!password.matches("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})")) {
                tints.setTintOnEditText(context, fieldPassword);
                throw new Exception("Password must contain 1 uppercase, 1 lowercase and 1 digit");
            } else if (!password.equals(confirmPassword)) {
                tints.setTintOnEditText(context, fieldConfirmPassword);
                throw new Exception("Passwords don't match");
            } else if (maxIncome.length() == 0) {
                tints.setTintOnEditText(context, fieldMaxIncome);
                throw new Exception("Income cannot be zero");
            } else if (!termsAreChecked) {
                throw new Exception("Please agree to terms and conditions");
            }
            return true;
        } catch (Exception e) {
            snackbar.showStatus(e.getMessage());
            return false;
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager.isAcceptingText()) {
            assert getCurrentFocus() != null;
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
