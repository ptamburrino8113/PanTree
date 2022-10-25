package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class createAccountActivity extends AppCompatActivity {

    Button createAccountButton2;
    Button backButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAccountButton2 = findViewById(R.id.createAccountButton2);
        backButton2 = findViewById(R.id.backButton2);

        createAccountButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Creates an account with entered credentials",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        backButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Returns user to main screen",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}