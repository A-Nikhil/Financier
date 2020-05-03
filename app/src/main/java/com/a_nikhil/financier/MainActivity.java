package com.a_nikhil.financier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIXME: 04-05-2020 Adding Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Move to dashboard
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        } // else => Stay as is
    }

    public void gotoLogin(View v) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void gotoSignup(View v) {
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivity(signupIntent);
    }

}
