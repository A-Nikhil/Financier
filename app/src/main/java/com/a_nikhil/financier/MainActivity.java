package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.Category;
import com.a_nikhil.financier.commons.Expenditure;
import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button login = findViewById(R.id.log_sgn);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    public void gotoLogin(View v) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void gotoSignup(View v) {
//        Intent signupIntent = new Intent(this, SignupActivity.class);
//        startActivity(signupIntent);


        HashMap<String, Expenditure> expenditures = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Expenditure expenditure = new Expenditure("a", 25.0, "d", Category.CASUAL);
        Expenditure expenditure2 = new Expenditure("b", 26.0, "e", Category.FOOD);
        Expenditure expenditure3 = new Expenditure("c", 27.0, "f", Category.OUTDOORS);
        String generatedKey = RandomStringUtils.random(10, true, true);
        expenditures.put(generatedKey, expenditure);
        generatedKey = RandomStringUtils.random(10, true, true);
        expenditures.put(generatedKey, expenditure2);
        generatedKey = RandomStringUtils.random(10, true, true);
        expenditures.put(generatedKey, expenditure3);
        db.collection("users")
                .add(expenditures)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });

//        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//        User user = new User("a", "b", "c", "d", 25.0, "asdeee");
//        db.insertUser(user);
    }

}
