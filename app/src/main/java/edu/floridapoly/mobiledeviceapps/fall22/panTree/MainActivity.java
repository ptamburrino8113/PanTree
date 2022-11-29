package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageView panTreeLogo;
    EditText editTextEmailAddress;
    EditText editTextPassword;
    Button loginButton;
    Button forgotPasswordButton;
    Button createAccountButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panTreeLogo = findViewById(R.id.panTreeLogo);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        loginButton = findViewById(R.id.createAccountButton2);
        forgotPasswordButton = findViewById(R.id.backButton2);
        createAccountButton = findViewById(R.id.createAccountButton);
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String emaill = editTextEmailAddress.getText().toString().trim();
                String passs = editTextPassword.getText().toString().trim();

                auth.signInWithEmailAndPassword(emaill, passs)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();

                                    Intent intent = new Intent(MainActivity.this, homeActivity.class);
                                    intent.putExtra("email", emaill);
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });

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