package com.a_nikhil.financier;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;

import com.a_nikhil.financier.Fragments.ExpenditureFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class NewExpenditureActivity extends AppCompatActivity {

    private static final String TAG = "NewExpenditureActivity";

    EditText dateEditText;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle emailBundle = getIntent().getExtras();
        assert emailBundle != null;
        userEmail = emailBundle.getString("email");
        Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_new_expenditure);
        dateEditText = findViewById(R.id.NewExpenditureActivityDate);

        // Adding a DatePicker
        final Calendar c = Calendar.getInstance();
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                int mYear, mMonth, mDay;
                if (hasFocus) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenditureActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String dater = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            dateEditText.setText(dater);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                } else {
                    dateEditText.clearFocus();
                }
            }
        });

        findViewById(R.id.addNewExpenditureActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpenditure(view);
            }
        });
    }

    public void addNewExpenditure(View v) {
        Log.d(TAG, "addNewExpenditure: new expenditure");
        final EditText nameEditText = findViewById(R.id.NewExpenditureActivityName);
        final EditText amountEditText = findViewById(R.id.NewExpenditureActivityAmount);
        final Chip selectedCategory = findViewById(((ChipGroup) findViewById(R.id.CategoriesChipGroup)).getCheckedChipId());

        final String name = nameEditText.getText().toString();
        final String amount = amountEditText.getText().toString();
        final String date = dateEditText.getText().toString();
        final String category = selectedCategory.getText().toString();

        if (performValidation(name, amount, date, category, v)) {
//            showStatusAsSnackbar("Expenditure Added", v);
            Log.d(TAG, "addNewExpenditure: validation success");
            Bundle bundle = new Bundle();
            String[] data = new String[]{name, amount, date, category};
            bundle.putStringArray("newExpenditureData", data);
            bundle.putString("email", userEmail);
            ExpenditureFragment fragmentObject = new ExpenditureFragment();
            fragmentObject.setArguments(bundle);
        }
    }

    private boolean performValidation(String name, String amount, String date, String category, View v) {
        Toast.makeText(this, "Inside validations", Toast.LENGTH_SHORT).show();
        try {
            if (name == null || name.length() == 0) {
                throw new Exception("Please enter a valid title");
            } else if (Double.parseDouble(amount) <= 0.0) {
                throw new Exception("Please enter a valid amount");
            } else if (category.equals("Select a category..")) {
                throw new Exception("Select a Category");
            } else if (date == null) {
                throw new Exception("Enter a valid date");
            }
            return true;
        } catch (Exception e) {
            Log.d(TAG, "performValidation: " + e.getMessage());
            showStatusAsSnackbar(e.getMessage(), v);
            return false;
        }
    }

    private void showStatusAsSnackbar(String status, View v) {
        final Snackbar infoSnackbar = Snackbar.make(v, status, Snackbar.LENGTH_SHORT);
        infoSnackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoSnackbar.dismiss();
            }
        });

        // Setting elevation of snackbar
        infoSnackbar.getView().setFitsSystemWindows(false);
        ViewCompat.setOnApplyWindowInsetsListener(infoSnackbar.getView(), null);

        // Setting color of Dismiss text
        infoSnackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.accent));

        // Setting snackbar text color
        ((TextView) infoSnackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        infoSnackbar.show();
    }

    public void cancelActivity(View v) {

    }
}