package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class sharesActivity extends AppCompatActivity {

    Button newUserButton;
    Button sharesButton;
    Button homeButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shares);

        newUserButton = findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This button will add a new family member by finding their account",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This button will take the user to the shares page to see family members",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sharesActivity.this, home_page.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sharesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}