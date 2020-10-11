package com.a_nikhil.financier;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.AddAndRemoveTints;
import com.a_nikhil.financier.commons.ConnectionStatus;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        collection = getResources().getString(R.string.collection);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    public void clickLogin(View v) {

        // CHECKPOINT: Checking Internet Connection
        if (new ConnectionStatus().isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final AddAndRemoveTints tints = new AddAndRemoveTints();
        final Context context = getApplicationContext();
        final EditText emailField = findViewById(R.id.login_email);
        final String email = emailField.getText().toString();
        final EditText passwordField = findViewById(R.id.login_password);
        final String password = passwordField.getText().toString();

        // CHECKPOINT: Validate Inputs
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // FIXME: 09-05-2020 Trying out new login
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
                                    // CHECKPOINT: Send intent to dashboard
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
                        Toast.makeText(LoginActivity.this, "Wrong Combination", Toast.LENGTH_SHORT).show();
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
                                // CHECKPOINT: User exists
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
                            }
                        } else {
                            Log.d("LoginWindow", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // CHECKPOINT: Adding the current logged in user to local db
    private void addToCache(User user, DatabaseHelper db) {
        if (db.insertUser(user)) {
            Log.d("Login Activity", "Added to cache");
        }
    }

    private interface LoginCallback {
        void onCallback(User user);
    }
}
