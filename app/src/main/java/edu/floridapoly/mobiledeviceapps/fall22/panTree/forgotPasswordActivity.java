package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPasswordActivity extends AppCompatActivity {

    Button sendLinkButton;
    Button backButton;
    EditText emailComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendLinkButton = findViewById(R.id.sendLinkButton);
        backButton = findViewById(R.id.backButton);
        emailComponent = findViewById(R.id.forgotPassEmailAddress);

        sendLinkButton.setOnClickListener(view -> {
            String email_user = emailComponent.getText().toString();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email_user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "A recovery email has been sent.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(forgotPasswordActivity.this, loginActivity.class);
                    startActivity(intent);
                }
            });
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(forgotPasswordActivity.this, loginActivity.class);
            startActivity(intent);
        });


    }
}