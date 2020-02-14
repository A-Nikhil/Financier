package com.a_nikhil.financier;

import android.app.ActionBar;
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
import com.a_nikhil.financier.commons.ConnectionStatus;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        ActionBar bar = getActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    public void clickLogin(View v) {

        // LOGIC HINT: Checking Internet Connection
        if (new ConnectionStatus().isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        // LOGIC HINT: Validate Inputs
        try {
            if (email.length() == 0) {
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw new Exception("Please enter a valid email address");
            } else if (password.length() == 0) {
                throw new Exception("Password cannot be empty");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }


        final LoginActivity activityObject = new LoginActivity();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserData(db, new LoginCallback() {
            @Override
            public void onCallback(HashMap<String, Map<String, Object>> userList) {
                // LOGIC HINT: Performing validation
                Log.d("clickLogin", "onCallback: Going to performLogin()");
                User returnedUser = activityObject.performLogin(email, password, userList);
                if (returnedUser != null) {
                    Toast.makeText(getApplicationContext(), "Hello " + returnedUser.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_SHORT).show();
                }

                // LOGIC HINT: Adding to local DB
                activityObject.addToCache(returnedUser);

                // LOGIC HINT: Send intent to dashboard
                startActivity(new Intent(LoginActivity.this, Dashboard.class));
            }
        });
    }


    private void getUserData(FirebaseFirestore db, final LoginCallback loginCallback) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                HashMap<String, Map<String, Object>> userList = new HashMap<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("LoginWindow", document.getId() + " => " + document.getData());
                                    userList.put(document.getId(), document.getData());
                                }
                                loginCallback.onCallback(userList);
                            } else {
                                Log.d("LoginWindow", "Error getting results: ", task.getException());
                            }
                        } else {
                            Log.d("LoginWindow", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private interface LoginCallback {
        void onCallback(HashMap<String, Map<String, Object>> userList);
    }

    private User performLogin(String email, String password, HashMap<String, Map<String, Object>> userList) {
        User finalUser = new User(), tempUser;
        boolean userFound = false;
        Log.d("performLogin", "Sie = " + userList.size());
        for (Map.Entry<String, Map<String, Object>> entry1 : userList.entrySet()) {
            tempUser = new User();
            for (Map.Entry<String, Object> entry2 : entry1.getValue().entrySet()) {
                if (entry2.getKey().equals("name")) {
                    tempUser.setName(entry2.getValue().toString());
                }
                if (entry2.getKey().equals("email")) {
                    tempUser.setEmail(entry2.getValue().toString());
                }
                if (entry2.getKey().equals("password")) {
                    tempUser.setPassword(entry2.getValue().toString());
                }
                if (entry2.getKey().equals("phone")) {
                    tempUser.setPhone(entry2.getValue().toString());
                }
            }
            Log.d("performLogin", "performLogin: " + tempUser.toString());
            if (tempUser.getEmail().equals(email) && tempUser.getPassword().equals(password)) {
                userFound = true;
                finalUser = tempUser;
                break;
            }
        }
        if (userFound) {
            return finalUser;
        } else {
            return null;
        }
    }

    // LOGIC HINT: Adding the current logged in user to local db
    private void addToCache(User user) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if (db.insertUser(user)) {
            Log.d("Login Activity", "Added to cache");
        }
    }
}
