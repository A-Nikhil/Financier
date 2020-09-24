package com.a_nikhil.financier.commons;

import android.content.Context;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.a_nikhil.financier.R;

public class AddAndRemoveTints {
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
