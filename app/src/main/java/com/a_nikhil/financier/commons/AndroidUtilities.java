package com.a_nikhil.financier.commons;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.a_nikhil.financier.R;
import com.google.android.material.snackbar.Snackbar;

public class AndroidUtilities {
    public static class ConnectionStatus {
        public boolean isNetworkConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo == null || !networkInfo.isConnectedOrConnecting();
        }
    }

    public static class AddAndRemoveTints {
        public void setTintOnEditText(Context context, EditText textField) {
            DrawableCompat.setTint(textField.getBackground(),
                    ContextCompat.getColor(context, R.color.duplicateEmail));

            // setting Text Color
            textField.setTextColor(ContextCompat.getColor(context, R.color.duplicateEmail));
        }

        public void removeTintFromEditText(Context context, EditText textField) {
            DrawableCompat.setTint(textField.getBackground(),
                    ContextCompat.getColor(context, R.color.accent));

            // setting Text Color
            textField.setTextColor(context.getColor(R.color.black));
        }
    }

    public static class ShowStatusAsSnackbar {
        private final Context context;
        private final View view;

        public ShowStatusAsSnackbar(Context context, View view) {
            this.context = context;
            this.view = view;
        }

        public void showStatus(String status) {
            final Snackbar infoSnackbar = Snackbar.make(this.view, status, Snackbar.LENGTH_SHORT);
            infoSnackbar.setAction("Dismiss", view -> infoSnackbar.dismiss());

            // Setting color of Dismiss text
            infoSnackbar.setActionTextColor(ContextCompat.getColor(this.context, R.color.accent));

            // Setting snackbar text color
            ((TextView) infoSnackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            infoSnackbar.show();
        }
    }
}
