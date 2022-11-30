package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button sharesButton;
    Button homeButton;
    Button settingsButton;
    EditText accesscodetext;
    Button refreshsharebutton;
    Button adduserbutton;
    ArrayList<String> accessCodesList;
    TextView accesscode;
    private FirebaseAuth mAuth;
    String email_user;
    String user_uid;
    ListView listView;
    
    @SuppressLint("MissingInflatedId")
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
        listView = findViewById(R.id.sharelist);
        Map<String, Object> lists = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();

        if (extras != null) {
            email_user = extras.getString("email");
            user_uid = extras.getString("uid_user");
            System.out.println(email_user);
        }

        accesscode.setText(user_uid);

        refreshsharebutton.setOnClickListener(view -> {
            String accessCodeSelf = accesscode.getText().toString();
            DocumentReference doc2Ref = db.collection("Access_codes").document(accessCodeSelf);
            doc2Ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("Access_codes data: "  + document.getData());

                        System.out.println("Access_codes data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                        Collection<Object> values = document.getData().values();

                        // create the string arraylist
                        accessCodesList = new ArrayList<>();

                        // loop over the objects in the collection and convert them to strings
                        // then add them to the arraylist
                        for(Object object : values){
                            accessCodesList.add(object.toString());
                        }
                        System.out.println("Access_codes list: " + accessCodesList.toString());
                        //update adapter
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, accessCodesList);
                        listView.setAdapter(arrayAdapter);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        });
        adduserbutton.setOnClickListener(view -> {
            String accessCodeValue = accesscodetext.getText().toString();
            String accessCodeSelf = accesscode.getText().toString();
            // TODO: what does this if statement do??
            if (accessCodeValue.matches("")) {}
            else
            {
                //to commit
                DocumentReference doc2Ref = db.collection("Access_codes").document(accessCodeSelf);
                doc2Ref.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Random r = new Random();
                            int i = r.nextInt(1000000);
                            String s1 = "code_" + i;
                            lists.put(s1, accessCodeValue);
                            db.collection("Access_codes").document(accessCodeSelf).update(lists);
                            refreshsharebutton.callOnClick();
                        }
                        else {
                            Log.d(TAG, "No such document");
                            lists.put("code_0", accessCodeValue);
                            db.collection("Access_codes").document(accessCodeSelf).set(lists, SetOptions.merge());
                            refreshsharebutton.callOnClick();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());

                    }
                });
                accesscodetext.setText("");
            }


        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "This button will take the user to the shares page to see family members",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(sharesActivity.this, homeActivity.class);
            startActivity(intent);
        });

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(sharesActivity.this, settingActivity.class);
            startActivity(intent);
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