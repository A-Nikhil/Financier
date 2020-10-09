package com.a_nikhil.financier;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class NewExpenditureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expenditure);
        final ChipGroup chipGroup = findViewById(R.id.chipGroup);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = chipGroup.getCheckedChipId();
                Chip chip = findViewById(id);
                Toast.makeText(NewExpenditureActivity.this, "Chip selected : " + chip.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}