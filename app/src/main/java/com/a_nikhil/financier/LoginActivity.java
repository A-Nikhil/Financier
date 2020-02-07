package com.a_nikhil.financier;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    }

    private HashMap<String, Map<String, Object>> dbUsers = new HashMap<>();

    public void clickLogin(View v) {
        String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserData(db, new LoginCallback() {
            @Override
            public void onCallback(HashMap<String, Map<String, Object>> userList) {
                dbUsers = userList;
                Log.d("LoginWindow", "Size = " + dbUsers.size());
                Toast.makeText(getApplicationContext(), "Size = " + dbUsers.size(), Toast.LENGTH_LONG).show();
                // FIXME: 07-02-2020 Move Stuff here
            }
        });

//        for (Map.Entry<String, Map<String, Object>> entry1 : userList.entrySet()) {
//            tempUser = new User();
//            for (Map.Entry<String, Object> entry2 : entry1.getValue().entrySet()) {
//                if (entry2.getKey().equals("name")) {
//                    tempUser.setName(entry2.getValue().toString());
//                }
//                if (entry2.getKey().equals("email")) {
//                    tempUser.setEmail(entry2.getValue().toString());
//                }
//                if (entry2.getKey().equals("password")) {
//                    tempUser.setPassword(entry2.getValue().toString());
//                }
//                if (entry2.getKey().equals("phone")) {
//                    tempUser.setPhone(entry2.getValue().toString());
//                }
//            }
//            if (tempUser.getEmail().equals(email) && tempUser.getPassword().equals(password)) {
//                userFound = true;
//                finalUser = tempUser;
//                break;
//            }
//        }
//
//        if (!userFound) {
//            Toast.makeText(this, "Invalid email/password", Toast.LENGTH_SHORT).show();
//        }

        // FIXME: 06-02-2020 Send intent to dashboard
        // FIXME: 06-02-2020 Add caching here
    }

    public void findUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getUserData(db, new LoginCallback() {
            @Override
            public void onCallback(HashMap<String, Map<String, Object>> userList) {
                dbUsers = userList;
                Log.d("LoginWindow", "Size = " + dbUsers.size());
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("LoginWindow", document.getId() + " => " + document.getData());
                                dbUsers.put(document.getId(), document.getData());
                            }
                            loginCallback.onCallback(dbUsers);
                        } else {
                            Log.d("LoginWindow", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private interface LoginCallback {
        void onCallback(HashMap<String, Map<String, Object>> userList);
    }
}
