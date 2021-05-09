package com.example.matchappproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String username;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    int gameState = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializes shared preferences files
        sharedPref = this.getSharedPreferences("prefs", this.MODE_PRIVATE);

        // Reads the username from the shared preference file. If no value for the key
        // R.string.username exists, sets the default username to "Player"
        username = sharedPref.getString(getString(R.string.username), getString(R.string.player));
        Log.i("username", "username is " + username);

        gameState = 0;
        Log.i("game_state", "Not in a game");

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            Log.d("AUTH", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });


        //

    }

    private Menu optionsMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public Menu getMenu() {
        return optionsMenu;
    }

    public void setGameState(int state) {
        gameState = state;
    }

    public int getGameState() {
        return gameState;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("Action_bar", "Item id is " + id);

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("game_state", "game state is " + gameState);

        if (gameState == 0) {
            super.onBackPressed();
            toolbar.setTitle("");
            MenuItem settings = optionsMenu.findItem(R.id.action_settings);
            settings.setVisible(true);
        }
        else {
            DialogFragment dialog = new ExitGameDialog();
            dialog.show(getSupportFragmentManager(), "ExitGameDialog");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("config", "calling config change");
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}