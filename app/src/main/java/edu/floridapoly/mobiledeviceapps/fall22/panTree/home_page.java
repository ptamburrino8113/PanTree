package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class home_page extends AppCompatActivity {

    Button homeButton;
    Button sharesButton;
    Button logoutButton;
    private FirebaseAuth mAuth;
    String email;


    // initialize home_page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        if (extras != null)
        {
            String emailtext = extras.getString("email");
            email = emailtext;
        }


        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Home page where you can view your pantry items. A quantity will later be added to each",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, sharesActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
}