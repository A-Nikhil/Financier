package com.a_nikhil.financier.commons;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtils {

    private boolean registrationSuccess = false;
    private String failMessage = "";

    public String[] registerNewUser(User user) {
        registrationSuccess = false;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String collection = "users";
        firestore.collection(collection)
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
}
