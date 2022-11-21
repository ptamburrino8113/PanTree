package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home_page extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    Button AddItem;
    EditText ItemName;
    ArrayAdapter<String> arrayAdapter;
    Button homeButton;
    Button sharesButton;
    Button logoutButton;
    private FirebaseAuth mAuth;
    String email_user;
    int ccounter = 0;
    int list_counter = 0;


    // initialize home_page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listView = (ListView) findViewById(R.id.listview);
        AddItem = (Button) findViewById(R.id.AddItem);
        ItemName = (EditText) findViewById(R.id.ItemName);
        list = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        Map<String, Object> lists = new HashMap<>();
        Map<String, Object> counter = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        if (extras != null) {
            String emailtext = extras.getString("email");
            email_user = emailtext;
            System.out.println(email_user);
        }

        AddItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String items = ItemName.getText().toString();
                if (items.matches("")) {}
                else
                {
                    DocumentReference docRef = db.collection("Counter").document(email_user);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    int gg = document.getLong("counter").intValue();
                                    ccounter = gg;
                                    System.out.println("this is ccounter in counter get");
                                    System.out.println(ccounter);
                                    counter.put("counter", ccounter);
                                    db.collection("Counter").document(email_user).update("counter", FieldValue.increment(1));


                                } else {
                                    Log.d(TAG, "No such document");
                                    counter.put("counter", 1);
                                    db.collection("Counter").document(email_user).set(counter, SetOptions.merge());
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });

                    DocumentReference doc2Ref = db.collection("Lists").document(email_user);
                    doc2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    System.out.println("this is list inside doc exists");
                                    String s1 = String.valueOf(ccounter);
                                    String s2 = ("list_item" + s1);
                                    System.out.println(s2);
                                    System.out.println(document.getData());
                                    lists.put(s2, items);
                                    db.collection("Lists").document(email_user).set(lists)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });


                                } else {
                                    Log.d(TAG, "No such document");
                                    lists.put("list_item1", items);
                                    db.collection("Lists").document(email_user).set(lists, SetOptions.merge());
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });
                    ItemName.setText("");
                    list.add(items);
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();

                }
            }
        });


        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Home page where you can view your pantry items. A quantity will later be added to each",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, sharesActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(home_page.this, MainActivity.class);
                //startActivity(intent);
            }
        });
    }

    //end of oncreate


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }






}