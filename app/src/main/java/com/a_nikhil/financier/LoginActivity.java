package com.a_nikhil.financier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.a_nikhil.financier.commons.FirestoreUtils;
import com.a_nikhil.financier.commons.User;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickLogin(View v) {
        HashMap<String, Map<String, Object>> userList = new FirestoreUtils().findUsers();
        String email =((EditText) findViewById(R.id.login_email)).getText().toString();
        String password =((EditText) findViewById(R.id.login_password)).getText().toString();


        boolean userFound = false;
        User tempUser, finalUser;
        Toast.makeText(this, userList.size(), Toast.LENGTH_LONG).show();


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
}
