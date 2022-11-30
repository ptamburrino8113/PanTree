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
        setContentView(R.layout.activity_main);

        panTreeLogo = findViewById(R.id.panTreeLogo);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        loginButton = findViewById(R.id.createAccountButton2);
        forgotPasswordButton = findViewById(R.id.backButton2);
        createAccountButton = findViewById(R.id.createAccountButton);
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            //to commit
            String emailClean = editTextEmailAddress.getText().toString().trim();
            String passClean = editTextPassword.getText().toString().trim();

            auth.signInWithEmailAndPassword(emailClean, passClean)
                    .addOnCompleteListener(loginActivity.this, task -> {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Intent intent = new Intent(loginActivity.this, homeActivity.class);
                            intent.putExtra("email", emailClean);
                            startActivity(intent);
                            //updateUI(user);
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