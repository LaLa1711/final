package com.example.topic1projectmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch estimateDaySwitch, musicSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        estimateDaySwitch = findViewById(R.id.estimateDaySwitch);
        musicSwitch = findViewById(R.id.musicSwitch);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        boolean isEstimateDayVisible = sharedPreferences.getBoolean("EstimateDayVisible", true);
        boolean isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true);

        estimateDaySwitch.setChecked(isEstimateDayVisible);
        musicSwitch.setChecked(isMusicEnabled);

        if (isMusicEnabled) {
            Intent musicServiceIntent = new Intent(SettingsActivity.this, MusicService.class);
            startService(musicServiceIntent);
        }

        estimateDaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("EstimateDayVisible", isChecked);
            editor.apply();
            Toast.makeText(SettingsActivity.this, "Estimate Day visibility changed", Toast.LENGTH_SHORT).show();
        });

        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("MusicEnabled", isChecked);
            editor.apply();

            if (isChecked) {
                Toast.makeText(SettingsActivity.this, "Music enabled", Toast.LENGTH_SHORT).show();
                Intent musicServiceIntent = new Intent(SettingsActivity.this, MusicService.class);
                startService(musicServiceIntent);
            } else {
                Toast.makeText(SettingsActivity.this, "Music disabled", Toast.LENGTH_SHORT).show();
                Intent musicServiceIntent = new Intent(SettingsActivity.this, MusicService.class);
                stopService(musicServiceIntent);
            }
        });
    }
}
