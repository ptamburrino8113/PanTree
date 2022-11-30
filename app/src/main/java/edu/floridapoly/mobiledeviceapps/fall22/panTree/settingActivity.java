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
        setContentView(R.layout.acitvity_settings);

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "This button will take the user to the shares page to see family members",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(settingActivity.this, homeActivity.class);
            startActivity(intent);
        });

    }
}
