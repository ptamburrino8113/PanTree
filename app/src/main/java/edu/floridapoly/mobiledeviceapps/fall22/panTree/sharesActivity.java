package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class sharesActivity extends AppCompatActivity {

    Button newUserButton;
    Button sharesButton;
    Button homeButton;
    Button logoutButton;
    EditText accesscodetext;
    Button refreshsharebutton;
    Button adduserbutton;
    ArrayList<String> list;
    TextView accesscode;
    LinearLayout familyMembers;
    private FirebaseAuth mAuth;
    // Initial test idea of having a text view count
    public int familyCount = 0;
    // Temporary first and last names to set the text view information as I couldn't
    // add it in the button press logic
    public String tempFName = "";
    public String tempLName = "";
    String email_user;
    String user_uid;
    ListView listView;

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

        refreshsharebutton = findViewById(R.id.refreshsharesbutton);
        accesscodetext = findViewById(R.id.accesscodetext);
        accesscode = findViewById(R.id.accesscode);
        // LinearLayout the family members are being added to
        adduserbutton = findViewById(R.id.adduserbutton);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listView = (ListView) findViewById(R.id.sharelist);
        Map<String, Object> lists = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();

        if (extras != null) {
            email_user = extras.getString("email");
            user_uid = extras.getString("uid_user");
            System.out.println(email_user);
        }

        accesscode.setText(user_uid);



        refreshsharebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String accesscode_me = accesscode.getText().toString();
                DocumentReference doc2Ref = db.collection("Access_codes").document(accesscode_me);
                doc2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                System.out.println("Document data: "  + document.getData());

                                System.out.println("Document data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                                Collection<Object> values = document.getData().values();
//                                System.out.println("Values : " + values.toString());

                                // create the string arraylist
                                list = new ArrayList<String>();

                                // loop over the objects in the collection and convert them to strings
                                // then add them to the arraylist
                                for(Object object : values){
                                    list.add(object.toString());
                                }
                                System.out.println("list: " + list.toString());
                                //update adapter
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                                listView.setAdapter(arrayAdapter);
                            }
                            else {

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());

                        }
                    }
                });



            }
        });
        adduserbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String acesscodeusertext = accesscodetext.getText().toString();
                String accesscode_me = accesscode.getText().toString();
                if (acesscodeusertext.matches("")) {}
                else
                {
                    //to commit
                    DocumentReference doc2Ref = db.collection("Access_codes").document(accesscode_me);
                    doc2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Random r = new Random();
                                    int i = r.nextInt(1000000 - 0);
                                    String s1 = "code_" + i;
                                    lists.put(s1, acesscodeusertext);
                                    db.collection("Access_codes").document(accesscode_me).update(lists);
                                    refreshsharebutton.callOnClick();
                                }
                                else {
                                    Log.d(TAG, "No such document");
                                    lists.put("code_0", acesscodeusertext);
                                    db.collection("Access_codes").document(accesscode_me).set(lists, SetOptions.merge());
                                    refreshsharebutton.callOnClick();
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });
                    accesscodetext.setText("");
                }


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
                Intent intent = new Intent(sharesActivity.this, homeActivity.class);
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
    @Override
    public void onStart() {
        super.onStart();
        refreshsharebutton.callOnClick();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
}