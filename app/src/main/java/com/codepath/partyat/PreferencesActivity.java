package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PreferencesActivity extends AppCompatActivity {
    private static final String TAG = "PreferencesActivity";
    private Switch mIsPrivate;
    private Switch mIsWeekend;
    private EditText mMaxDistance;
    private EditText mMaxPrice;
    private Button mSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mIsPrivate = findViewById(R.id.sPrivate);
        mIsWeekend = findViewById(R.id.sWeekend);
        mMaxDistance = findViewById(R.id.etMaxDistance);
        mMaxPrice = findViewById(R.id.etMaxPrice);
        mSave = findViewById(R.id.btnSavePref);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(PreferencesActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void savePreferences() {
        Preference pref = new Preference();
        if (mMaxDistance.getText().toString().isEmpty()) {
            Toast.makeText(this, "Max Distance cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mMaxPrice.getText().toString().equals("0")) {
            Toast.makeText(this, "Max Price cannot be 0", Toast.LENGTH_LONG).show();
            return;
        }
        pref.setIsPrivate(mIsPrivate.isChecked());
        pref.setIsWeekend(mIsWeekend.isChecked());
        pref.setMaxDistance(Integer.parseInt(mMaxDistance.getText().toString()));
        pref.setMaxPrice(Integer.parseInt(mMaxPrice.getText().toString()));
        pref.setUser(ParseUser.getCurrentUser());
        pref.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving!", e);
                    Toast.makeText(PreferencesActivity.this, "Error while saving!", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(PreferencesActivity.this, "Preferences saved successfully", Toast.LENGTH_LONG).show();
                mMaxDistance.setText("");
                finish();
            }
        });
    }
}