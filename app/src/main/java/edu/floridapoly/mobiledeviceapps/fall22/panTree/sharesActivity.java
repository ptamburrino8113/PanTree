package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class sharesActivity extends AppCompatActivity {

    Button newUserButton;
    Button sharesButton;
    Button homeButton;
    Button logoutButton;
    LinearLayout familyMembers;
    // Initial test idea of having a text view count
    public int familyCount = 0;
    // Temporary first and last names to set the text view information as I couldn't
    // add it in the button press logic
    public String tempFName = "";
    public String tempLName = "";

    // Activity result launcher to get first and last name from "Add Family Member" activity
    // The goal is that once recieved, the first name and last name will populate the dynamically added
    // TextView on line 93 and a family member will be added
    // Planning to add a "-" button to remove a family member
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent returnIntent = result.getData();
                        boolean enter = returnIntent.getBooleanExtra("Entered",false);
                        String fName = returnIntent.getStringExtra("FirstName");
                        String lName = returnIntent.getStringExtra("LastName");
                        if (enter)
                        {
                            // Initial test with toast message to verify it's getting a return value
                            Toast.makeText(getBaseContext(),"Retured from addFamilyMember, Recieved: Entered",Toast.LENGTH_LONG).show();
                            // Return values from addFamilyMember
                            tempFName = fName;
                            tempLName = lName;
                        }
                        else
                        {
                            //do nothing
                        }
                    }
                }
            });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shares);

        newUserButton = findViewById(R.id.newUserButton);
        // LinearLayout the family members are being added to
        familyMembers = findViewById(R.id.familyMembers);

        // Button to send to addFamilyMembers activity
        newUserButton = findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newUserIntent = new Intent(sharesActivity.this, addFamilyMember.class);
                familyCount++;
                activityResultLauncher.launch(newUserIntent);
            }
        });

        // Logic to handle dynamically creating a TextView when a family member was added, not fully
        // completed just a test currently

        // As of now the text view being added isn't happening when returning from addFamilyMembers. I
        // believe it's because the method is running in the onCreate phase and the execution order hasn't
        // recieved the return information from addFamilyMembers, but I didn't have time to test it in this build

        // These can also be buttons if we were to add an information screen for each family member
        // If anyone can think of/find a better way to do this please go ahead
        if (familyCount>0) {
            TextView tv = new TextView(this);
            // To my understanding is setting default parameters of the linear layout
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // After default parameters are set we can edit the dynamically created Text View's attributes and then display it
            tv.setText(tempFName);
            // Haven't figured out how to change parameters of the text view to match the demo view in activity_shares.xml
            this.familyMembers.addView(tv);
        }

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This button will take the user to the shares page to see family members",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sharesActivity.this, home_page.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sharesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}