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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

        createAccountButton2 = (Button)findViewById(R.id.createAccountButton2);
        backButton2 = findViewById(R.id.backButton2);
        createAccountButton2 = findViewById(R.id.createAccountButton2);
        EditText email = findViewById(R.id.editTextEmailAddress2);
        EditText pass = findViewById(R.id.editTextPassword2);
        System.out.println(email);
        System.out.println(pass);


        createAccountButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String emaill = email .getText().toString().trim();
                String passs = pass .getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(emaill, passs)
                        .addOnCompleteListener(createAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    // Sign in success, update UI with the signed-in user's information
                                    //adsadas
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(createAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(createAccountActivity.this, homeActivity.class);
                                    intent.putExtra("email", emaill);
                                    Map<String, Object> userr = new HashMap<>();
                                    userr.put("email", emaill);
                                    userr.put("pass", passs);
                                    db.collection("Users").document(user.getUid()).set(userr, SetOptions.merge());

                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(createAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });

            }
        });


        backButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(createAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

}