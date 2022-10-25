package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView panTreeLogo;
    EditText editTextEmailAddress;
    EditText editTextPassword;
    Button loginButton;
    Button forgotPasswordButton;
    Button createAccountButton;
    Button loginMessageButton;
    Button forgotPasswordMessageButton;
    Button createAccountMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panTreeLogo = findViewById(R.id.panTreeLogo);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        loginButton = findViewById(R.id.createAccountButton2);
        forgotPasswordButton = findViewById(R.id.backButton2);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginMessageButton = findViewById(R.id.loginMessageButton);
        forgotPasswordMessageButton = findViewById(R.id.forgotPasswordMessageButton);
        createAccountMessageButton = findViewById(R.id.createAccountMessageButton);

        loginMessageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This button attempts to match the credentials with database information and if accepted logs the user in",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        forgotPasswordMessageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Sends user to a screen allowing them to reset their password",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        createAccountMessageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Sends user to a screen allowing them to create an account",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, home_page.class);
                startActivity(intent);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, forgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, createAccountActivity.class);
                startActivity(intent);
            }
        });

        /*
        cheatButton = findViewById(R.id.buttonCheat);
        cheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CheatActivity.class);
                intent.putExtra(EXTRA_ANSWER, mQuestionBank[currentQuestion].isAnswerTrue());
                //startActivity(intent);

                activityResultLauncher.launch(intent);
            }
        });
         */
    }
}