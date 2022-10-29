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


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, home_page.class);
                intent.putExtra("Username", editTextEmailAddress.getText().toString());
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