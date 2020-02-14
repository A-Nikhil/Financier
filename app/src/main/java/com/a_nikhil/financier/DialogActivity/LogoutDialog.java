package com.a_nikhil.financier.DialogActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.a_nikhil.financier.MainActivity;
import com.a_nikhil.financier.R;
import com.a_nikhil.financier.caching.DatabaseHelper;

public class LogoutDialog extends AppCompatDialogFragment {

    private DatabaseHelper db;
    public LogoutDialog(DatabaseHelper db) {
        this.db = db;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View logoutView = layoutInflater.inflate(R.layout.layout_logout, null);
        dialog.setView(logoutView);
        final AlertDialog alertDialog = dialog.create();
        logoutView.findViewById(R.id.positiveLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.wipeClean();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        logoutView.findViewById(R.id.negativeLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
}
