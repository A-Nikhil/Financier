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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.AndroidUtilities;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String collection;
    private AndroidUtilities.ShowStatusAsSnackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        collection = getResources().getString(R.string.collection);
        snackbar = new AndroidUtilities.ShowStatusAsSnackbar(getApplicationContext(), findViewById(R.id.login_screen));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    public void clickLogin(View v) {

        // Hiding Soft Keyboard
        hideSoftKeyboard();

        //  Checking Internet Connection
        if (new AndroidUtilities.ConnectionStatus().isNetworkConnected(getApplicationContext())) {
            snackbar.showStatus("No Internet Connection");
            return;
        }

        final AndroidUtilities.AddAndRemoveTints tints = new AndroidUtilities.AddAndRemoveTints();
        final Context context = getApplicationContext();
        final EditText emailField = findViewById(R.id.login_email);
        final String email = emailField.getText().toString();
        final EditText passwordField = findViewById(R.id.login_password);
        final String password = passwordField.getText().toString();

        //  Validate Inputs
        tints.removeTintFromEditText(context, emailField);
        tints.removeTintFromEditText(context, passwordField);
        try {
            if (email.length() == 0) {
                tints.setTintOnEditText(context, emailField);
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tints.setTintOnEditText(context, emailField);
                throw new Exception("Please enter a valid email address");
            } else if (password.length() == 0) {
                tints.setTintOnEditText(context, passwordField);
                throw new Exception("Password cannot be empty");
            }
        } catch (Exception e) {
            snackbar.showStatus(e.getMessage());
            return;
        }

        final LoginActivity activityObject = new LoginActivity();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseHelper localDB = new DatabaseHelper(getApplicationContext());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firestoreUser = auth.getCurrentUser();
                        if (firestoreUser != null) {
                            Toast.makeText(getApplicationContext(), "Hello " + firestoreUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                            getUserData(db, firestoreUser.getEmail(), new LoginCallback() {
                                @Override
                                public void onCallback(User user) {
                                    activityObject.addToCache(user, localDB);
                                    //  Send intent to dashboard
                                    Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                    Bundle myBundle = new Bundle();
                                    myBundle.putString("email", user.getEmail());
                                    intent.putExtras(myBundle);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tints.setTintOnEditText(context, emailField);
                        tints.setTintOnEditText(context, passwordField);
                        snackbar.showStatus("Wrong Combination");
                    }
                });
    }

    private void getUserData(FirebaseFirestore db, final String email, final LoginCallback loginCallback) {
        db.collection(collection)
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                Map<String, Object> userWithEmail = document.getData();
                                assert userWithEmail != null;
                                //  User exists
                                User user = new User(
                                        String.valueOf(userWithEmail.get("name")),
                                        String.valueOf(userWithEmail.get("email")),
                                        String.valueOf(userWithEmail.get("phone")),
                                        String.valueOf(userWithEmail.get("password")),
                                        Double.parseDouble(String.valueOf(userWithEmail.get("maxIncome")))
                                );

                                loginCallback.onCallback(user);
                            } else {
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                snackbar.showStatus("User Not Found");
                            }
                        } else {
                            Log.d("LoginWindow", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //  Adding the current logged in user to local db
    private void addToCache(User user, DatabaseHelper db) {
        if (db.insertUser(user)) {
            Log.d("Login Activity", "Added to cache");
        }
    }

    private interface LoginCallback {
        void onCallback(User user);
    }

    private void hideSoftKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager.isAcceptingText()) {
            assert getCurrentFocus() != null;
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
