package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addFamilyMemberActivity extends AppCompatActivity {

    Button enterButton;
    EditText firstName;
    EditText lastName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);

        firstName = findViewById(R.id.editTextFName);
        lastName = findViewById(R.id.editTextLName);

        Intent i = getIntent();

        enterButton = findViewById(R.id.enterInfoButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                // Returns "Entered" value to sharesActivity
                returnIntent.putExtra("Entered", true);
                // Returns first and last name from editText to sharesActivity
                returnIntent.putExtra("FirstName", firstName.getText().toString());
                returnIntent.putExtra("LastName", lastName.getText().toString());
                setResult(RESULT_OK, returnIntent);
            }
        });
    }
}