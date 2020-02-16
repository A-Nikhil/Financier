package com.a_nikhil.financier.DialogActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.a_nikhil.financier.R;

public class NewExpenditureDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private Context context;

    public NewExpenditureDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder addNewDialog = new AlertDialog.Builder(getActivity());
        setStyle(DialogFragment.STYLE_NO_TITLE,
                android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View expenditureView = layoutInflater.inflate(R.layout.new_expenditure_layout, null);

        // LOGIC HINT: Add Spinner
        Spinner spinner = expenditureView.findViewById(R.id.categories);
        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(context,
                R.array.categories, android.R.layout.simple_spinner_item);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(this);

        addNewDialog.setView(expenditureView);
        final AlertDialog addNewAlertDialog = addNewDialog.create();
        expenditureView.findViewById(R.id.addNewExpenditure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
