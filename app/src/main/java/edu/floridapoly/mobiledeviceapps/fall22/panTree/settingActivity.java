package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settingActivity extends AppCompatActivity {

    Button homeButton;
    Button sharesButton;
    Button settingsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle extras = getIntent().getExtras();

        sharesButton = findViewById(R.id.sharesButton);
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

    }
}
