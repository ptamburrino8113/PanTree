package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

        panTreeLogo = findViewById(R.id.panTreeLogo);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        loginButton = findViewById(R.id.createAccountButton2);
        forgotPasswordButton = findViewById(R.id.backButton2);
        createAccountButton = findViewById(R.id.createAccountButton);
        auth = FirebaseAuth.getInstance();

//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
//        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
//
//        if (isDarkModeOn)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        loginButton.setOnClickListener(view -> {
            String emailClean = editTextEmailAddress.getText().toString().trim();
            String passClean = editTextPassword.getText().toString().trim();

            if(emailClean.length() == 0){
                Toast.makeText(loginActivity.this, "Email field is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(passClean.length() == 0){
                Toast.makeText(loginActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailClean, passClean)
                    .addOnCompleteListener(loginActivity.this, task -> {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            // Sign in success
                            Intent intent = new Intent(loginActivity.this, homeActivity.class);
                            intent.putExtra("email", user.getEmail());
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    });

        });

        forgotPasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(loginActivity.this, forgotPasswordActivity.class);
            startActivity(intent);
        });

        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(loginActivity.this, createAccountActivity.class);
            startActivity(intent);
        });
    }
}