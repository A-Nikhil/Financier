package com.a_nikhil.financier.DialogActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.a_nikhil.financier.R;

import java.util.Calendar;

public class NewExpenditureDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "NewExpenditureDialog";

    // CHECKPOINT: Interface to pass data back
    public interface OnItemInsert {
        void sendInput(String name, String amount, String date, String category);
    }

    private OnItemInsert mOnItemInsert;

    private Context context;

    public NewExpenditureDialog(Context context) {
        this.context = context;
    }

    // widgets
    private EditText name, date, amount;
    private Spinner categories;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder addNewDialog = new AlertDialog.Builder(getActivity());
        setStyle(DialogFragment.STYLE_NO_TITLE,
                android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View expenditureView = layoutInflater.inflate(R.layout.add_new_expenditure_layout, null);

        // linking widgets to IDs
        name = expenditureView.findViewById(R.id.NewExpenditureDialogName);
        date = expenditureView.findViewById(R.id.NewExpenditureDialogDate);
        amount = expenditureView.findViewById(R.id.NewExpenditureDialogAmount);

        // CHECKPOINT: Add Spinner
        categories = expenditureView.findViewById(R.id.categories);
        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(context,
                R.array.categories, android.R.layout.simple_spinner_item);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categoriesAdapter);
        categories.setOnItemSelectedListener(this);

        // CHECKPOINT: Adding a DatePicker to NewExpenditureDialogDate
        final Calendar c = Calendar.getInstance();
        date = expenditureView.findViewById(R.id.NewExpenditureDialogDate);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                int mYear, mMonth, mDay;
                if (hasFocus) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String dater = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            date.setText(dater);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                } else {
                    date.clearFocus();
                }
            }
        });

        addNewDialog.setView(expenditureView);
        final AlertDialog addNewAlertDialog = addNewDialog.create();
        expenditureView.findViewById(R.id.addNewExpenditure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categories.getSelectedItem().toString();
                if (performValidation(name.getText().toString(), amount.getText().toString(),
                        date.getText().toString(), category)) {
                    mOnItemInsert.sendInput(name.getText().toString(), amount.getText().toString(),
                            date.getText().toString(), category);
                }
            }
        });
        expenditureView.findViewById(R.id.cancelAddNewExpenditure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewAlertDialog.dismiss();
            }
        });
        return addNewAlertDialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnItemInsert = (OnItemInsert) getTargetFragment();
        } catch (ClassCastException e) {
            Log.d("NewExpenditureDialog", "onAttach: " + e.getMessage());
        }
    }

    private boolean performValidation(String name, String amount, String date, String category) {
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
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // do nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
