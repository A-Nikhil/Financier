package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.caching.DatabaseHelper;
import com.a_nikhil.financier.commons.User;

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
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//        db.wipeCleanTables();
        User user = new User("a", "b", "c", "d", 25.0, "asdeee");
        db.insertUser(user);
    }

}
