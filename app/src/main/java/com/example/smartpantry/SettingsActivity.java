package com.example.smartpantry;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.smartpantry.util.NavHelper;
import com.example.smartpantry.util.Prefs;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        
        SwitchCompat expirySwitch = findViewById(R.id.switch_expiry);
        expirySwitch.setChecked(Prefs.isExpiryAlertsOn(this));
        expirySwitch.setOnCheckedChangeListener((button, isChecked) ->
                Prefs.setExpiryAlertsOn(this, isChecked));

        Spinner unitSpinner = findViewById(R.id.spinner_default_unit);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
        int current = unitAdapter.getPosition(Prefs.getDefaultUnit(this));
        if (current >= 0) {
            unitSpinner.setSelection(current);
        }
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Prefs.setDefaultUnit(SettingsActivity.this, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        NavHelper.setup(this, R.id.nav_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.syncSelection(this, R.id.nav_settings);
    }
}
