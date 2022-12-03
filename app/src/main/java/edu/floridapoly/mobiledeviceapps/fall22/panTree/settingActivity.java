package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class settingActivity extends AppCompatActivity {

    Button homeButton;
    Button sharesButton;
    //Button settingsButton;
    Button logoutbutton;
    Switch btnToggleDark;

    /*  TODO:
        - fix dark mode toggle being scuffed between restarts
        - fix dark mode toggle on switching between screens
        - fix bottom button colors to look better, maybe highlight selected and leave the rest the same
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle extras = getIntent().getExtras();

        sharesButton = findViewById(R.id.sharesButton);
        logoutbutton = findViewById(R.id.logoutButton);
        btnToggleDark = findViewById(R.id.darkModeSwitch);


        sharesButton.setOnClickListener(view -> {
            Intent intent = new Intent(settingActivity.this, sharesActivity.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("uid_user", extras.getString("uid_user"));
            startActivity(intent);
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(settingActivity.this, homeActivity.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("uid_user", extras.getString("uid_user"));
            startActivity(intent);
        });
        logoutbutton.setOnClickListener(view -> {
            Intent intent = new Intent(settingActivity.this, loginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
        });


        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnToggleDark.setText("Disable Dark Mode");
            btnToggleDark.setChecked(true);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnToggleDark.setText("Enable Dark Mode");
            btnToggleDark.setChecked(false);
        }

        btnToggleDark.setOnClickListener(view -> {
            if (isDarkModeOn)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                btnToggleDark.setText("Enable Dark Mode");
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();
                btnToggleDark.setText("Disable Dark Mode");
            }
        });
    }
}
