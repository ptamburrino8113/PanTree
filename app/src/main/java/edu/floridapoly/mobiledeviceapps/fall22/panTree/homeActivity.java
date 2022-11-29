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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class homeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    Button AddItem;
    Button refreshbutton;
    EditText ItemName;
    ArrayAdapter<String> arrayAdapter;
    Button homeButton;
    Button sharesButton;
    Button logoutButton;
    private FirebaseAuth mAuth;
    String email_user;
    String unique_code;

    public int getCcounter() {
        return ccounter;
    }

    public int setCcounter(int ccounter)
    {
        this.ccounter = ccounter;
        return ccounter;
    }

    private int ccounter = 0;
    int list_counter;


    // initialize home_page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listView = (ListView) findViewById(R.id.listview);
        AddItem = (Button) findViewById(R.id.AddItem);
        refreshbutton = (Button) findViewById(R.id.refreshbutton);
        ItemName = (EditText) findViewById(R.id.ItemName);
        Map<String, Object> lists = new HashMap<>();
        Map<String, Object> counter = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid_user = currentFirebaseUser.getUid();


        if (extras != null) {
            email_user = extras.getString("email");
            System.out.println(email_user);
        }

        AddItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String items = ItemName.getText().toString();
                if (items.matches("")) {}
                else
                {
                    DocumentReference doc2Ref = db.collection("Lists").document(uid_user);
                    doc2Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Random r = new Random();
                                    int i = r.nextInt(1000000 - 0);
                                    String s1 = "list_item" + i;
                                    lists.put(s1, items);
                                    db.collection("Lists").document(uid_user).update(lists);
                                    refreshbutton.callOnClick();
                                }
                                else {
                                    Log.d(TAG, "No such document");
                                    lists.put("list_item0", items);
                                    db.collection("Lists").document(uid_user).set(lists, SetOptions.merge());
                                    refreshbutton.callOnClick();
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });
                    ItemName.setText("");
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

        refreshbutton = findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                DocumentReference doc3Ref = db.collection("Access_codes").document(uid_user);
                doc3Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        //to commit
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
                                    System.out.println(object.toString());


                                    DocumentReference doc2Ref = db.collection("Lists").document(object.toString());
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
                                                    //list = new ArrayList<String>();

                                                    // loop over the objects in the collection and convert them to strings
                                                    // then add them to the arraylist
                                                    for(Object object : values){
                                                        list.add(object.toString());
                                                    }
                                                    Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
                                                    System.out.println("list: " + list.toString());
                                                    //update adapter
                                                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);

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

                            }
                            else {

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());

                        }
                    }
                });




                DocumentReference doc2Ref = db.collection("Lists").document(uid_user);
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
                                Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
                                System.out.println("list: " + list.toString());
                                //update adapter
                                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
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

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(homeActivity.this, sharesActivity.class);
                intent.putExtra("email", email_user);
                intent.putExtra("uid_user", uid_user);
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
        refreshbutton.callOnClick();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }






}