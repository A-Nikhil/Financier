package com.a_nikhil.financier.commons;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtils {

    private boolean registrationSuccess = false;
    private String failMessage = "";

    public String[] registerNewUser(User user) {
        registrationSuccess = false;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collection = "users";
        db.collection(collection)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        registrationSuccess = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registrationSuccess = false;
                        failMessage = e.getMessage();
                    }
                });
        if (registrationSuccess) {
            return new String[]{"1", "Successfully Registered"};
        } else {
            return new String[]{"0", failMessage};
        }
    }

    private HashMap<String, Map<String, Object>> dbUsers = new HashMap<>();

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
