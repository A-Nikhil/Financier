package com.a_nikhil.financier;

import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a_nikhil.financier.commons.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        boolean termsAreChecked = ((CheckBox) findViewById(R.id.TermsAndConditions)).isChecked();

        // Validation
        try {
            if (name.length() == 0) {
                throw new Exception("Name cannot be empty");
            } else if (!name.matches("^[A-Za-z ]*$")) {
                throw new Exception("Name can only contain Alphabets and Spaces");
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
            } else if (!password.matches("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})")) {
                throw new Exception("Password must contain 1 uppercase, 1 lowercase and 1 digit");
            } else if (!password.equals(confirmPassword)) {
                throw new Exception("Passwords don't match");
            } else if (!termsAreChecked) {
                throw new Exception("Please agree to terms and conditions");
            }
            registerNewUser(new User(name, email, phone, password));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void registerNewUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collection = "users";
        db.collection(collection)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                        // FIXME: 08-02-2020 Add Intent to Dashboard
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
