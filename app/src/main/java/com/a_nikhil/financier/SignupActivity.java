package com.a_nikhil.financier;

import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView txt = findViewById(R.id.registerHeading);
        txt.setText(Html.fromHtml("<u>Registration</u>"));
    }

    public void clickRegistration(View v) {
        String name = ((EditText) findViewById(R.id.username)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPassword)).getText().toString();

        // Validation
        try {
            if (name.length() == 0) {
                throw new Exception("Username cannot be empty");
            } else if (!name.matches("[A-Za-z]*")) {
                throw new Exception("Username can only contain Alphabets");
            } else if (email.length() == 0) {
                throw new Exception("Email cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw new Exception("Please enter a valid email address");
            } else if (phone.length() == 0) {
                throw new Exception("Phone cannot be empty");
            } else if (!Patterns.PHONE.matcher(phone).matches()) {
                throw new Exception("Invalid phone number");
            } else if (password.length() == 0) {
                throw new Exception("Password cannot be empty");
            }
            // FIXME: 02-02-2020 FIX password issues
//            } else if (!(password.matches("[A-Z]+") && password.matches("[a-z]+") && password.matches("[0-9]+"))) {
//                throw new Exception("Password must contain 1 uppercase, 1 lowercase and 1 digit");
//            } else if (confirmPassword.length() == 0) {
//                throw new Exception("Confirm Password cannot be empty");
//            } else if (!confirmPassword.equals(password)) {
//                throw new Exception("Password and confirm password must be equal");
//            }
            registerToFirebase(name, email, phone, password);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void registerToFirebase(String name, String email, String phone, String password) {
        // FIXME: 02-02-2020 Configure firebase and cloud storage
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        User user = new User(name, email, phone, password);
        reference.child("users").child(name).setValue(user);
        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
    }
}
