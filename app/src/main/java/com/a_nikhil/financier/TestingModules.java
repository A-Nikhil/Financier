package com.a_nikhil.financier;

import android.util.Log;

import com.a_nikhil.financier.caching.DatabaseHelper;

public class TestingModules {

    private static final String TAG = "TestingModules";

    public void checkUser(DatabaseHelper db) {
        Log.i(TAG, "checkUser: " + db.getUserData().toString());
    }

    public void checkExpenditure(DatabaseHelper db) {
        Log.i(TAG, "checkExpenditure: " + db.getExpenditureData().toString());
    }
}
