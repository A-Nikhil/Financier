package com.a_nikhil.financier.commons;

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
    public HashMap<String, Map<String, Object>> findUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbUsers.put(document.getId(), document.getData());
                            }
                        }
                    }
                });
        if (dbUsers.size() > 0) {
            return dbUsers;
        } else {
            return null;
        }
    }
}
