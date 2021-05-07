package com.example.matchappproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText usernameET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        usernameET = (EditText) findViewById(R.id.editText_username);
        sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);
        usernameET.setText(sharedPref.getString(getString(R.string.username), "Player"));

    }

    private Menu optionsMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);

        return true;
    }

    public void saveClick(View save) {
        sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            editor.putString(getString(R.string.username), usernameET.getText().toString());
            editor.apply();
            Log.i("username", "username successfully written to shared preference");
            Toast.makeText(this, R.string.successfulUsernameChange, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i("username", "failed to write username to shared preference");
            Toast.makeText(this, R.string.failureUsernameChange, Toast.LENGTH_SHORT).show();
        }
    }

    public void menuClick(View menu) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}