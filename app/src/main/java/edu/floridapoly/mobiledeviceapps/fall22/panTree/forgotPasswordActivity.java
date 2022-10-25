package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class forgotPasswordActivity extends AppCompatActivity {

    Button sendLinkButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendLinkButton = findViewById(R.id.sendLinkButton);
        backButton = findViewById(R.id.backButton);

        sendLinkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Sends password reset link if account exists with email entered",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
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