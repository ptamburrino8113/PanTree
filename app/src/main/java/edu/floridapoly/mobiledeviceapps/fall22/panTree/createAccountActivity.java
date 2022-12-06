package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class createAccountActivity extends AppCompatActivity
{
    Button createAccountButton2;
    Button backButton2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        createAccountButton2 = findViewById(R.id.createAccountButton2);
        backButton2 = findViewById(R.id.backButton2);
        createAccountButton2 = findViewById(R.id.createAccountButton2);
        EditText emailComponent = findViewById(R.id.editTextEmailAddress2);
        EditText passwordComponent = findViewById(R.id.editTextPassword2);

        createAccountButton2.setOnClickListener(view -> {
            String emailValue = emailComponent.getText().toString().trim();
            String passwordValue = passwordComponent .getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(createAccountActivity.this, task -> {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(createAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(createAccountActivity.this, homeActivity.class);
                            intent.putExtra("email", emailValue);
                            Map<String, Object> userr = new HashMap<>();
                            userr.put("email", emailValue);
                            userr.put("pass", passwordValue);
                            assert user != null;
                            db.collection("Users").document(user.getUid()).set(userr, SetOptions.merge());
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(createAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        backButton2.setOnClickListener(view -> {
            Intent intent = new Intent(createAccountActivity.this, loginActivity.class);
            startActivity(intent);
        });
    }
}