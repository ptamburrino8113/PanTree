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
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class homeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items_list;
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
        listView = findViewById(R.id.listview);
        AddItem = findViewById(R.id.AddItem);
        refreshbutton = findViewById(R.id.refreshbutton);
        ItemName = findViewById(R.id.ItemName);
        Map<String, Object> lists = new HashMap<>();
        Map<String, Object> counter = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentFirebaseUser != null;
        String uid_user = currentFirebaseUser.getUid();

        if (extras != null) {
            email_user = extras.getString("email");
            System.out.println(email_user);
        }

        AddItem.setOnClickListener(view -> {
            String items = ItemName.getText().toString();
            //TODO: what does this if statement do??
            if (items.matches("")) {

            }
            else
            {
                DocumentReference doc2Ref = db.collection("Lists").document(uid_user);
                doc2Ref.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Random r = new Random();
                            int i = r.nextInt(1000000);
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
                });
                ItemName.setText("");
            }
        });



        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Home page where you can view your pantry items. A quantity will later be added to each",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        /* TODO: Implementation for the refresh could change for how we display lists
            - If we make a document for the access_code when we create the account,
            we could add ourselves to the list and then just use the first loop to grab everything
            instead of using two loops (one loop for everyone else list, one loop for ourself)


         */
        refreshbutton = findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(view -> {
            items_list = new ArrayList<>();
            DocumentReference doc3Ref = db.collection("Access_codes").document(uid_user);
            System.out.println("UID: " + uid_user);

            doc3Ref.get().addOnCompleteListener(task -> {
                //to commit
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("UID data: "  + document.getData());
                        System.out.println("UID data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                        Collection<Object> uidList = document.getData().values();
//                                System.out.println("Values : " + values.toString());

                        // create the string arraylist

                        // loop over the objects in the collection and convert them to strings
                        // then add them to the arraylist
                        for(Object uid_object : uidList){
                            System.out.println(uid_object.toString());


                            DocumentReference doc2Ref = db.collection("Lists").document(uid_object.toString());
                            doc2Ref.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document1 = task1.getResult();
                                    if (document1.exists()) {
                                        System.out.println("Document data: "  + document1.getData());
                                        System.out.println("Document data type: "  + Objects.requireNonNull(document1.getData()).getClass().getName());
                                        Collection<Object> items_collection = document1.getData().values();

                                        // loop over the objects in the collection and convert them to strings
                                        // then add them to the arraylist
                                        for(Object item : items_collection){
                                            items_list.add(item.toString());
                                        }
                                        Collections.sort(items_list, String.CASE_INSENSITIVE_ORDER);
                                        System.out.println("items list: " + items_list.toString());
                                        //update adapter
                                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items_list);

                                        listView.setAdapter(arrayAdapter);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task1.getException());
                                }
                            });
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());

                }
            });


            DocumentReference doc2Ref = db.collection("Lists").document(uid_user);
            doc2Ref.get().addOnCompleteListener(task -> {
                System.out.println("2 self print");
                if (task.isSuccessful()) {
                    System.out.println("3 self print");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("4 self print");
                        System.out.println("Document data: "  + document.getData());
                        System.out.println("Document data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                        Collection<Object> items_collection = document.getData().values();

                        // loop over the objects in the collection and convert them to strings
                        // then add them to the arraylist
                        for(Object item : items_collection){
                            homeActivity.this.items_list.add(item.toString());
                            System.out.println("self print: " + item);
                        }
                        System.out.println("5 self print");
                        Collections.sort(homeActivity.this.items_list, String.CASE_INSENSITIVE_ORDER);
                        System.out.println("list: " + homeActivity.this.items_list.toString());
                        //update adapter
                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, homeActivity.this.items_list);
                        listView.setAdapter(arrayAdapter);
                        System.out.println("6 self print");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());

                }
            });

        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(view -> {
            Intent intent = new Intent(homeActivity.this, sharesActivity.class);
            intent.putExtra("email", email_user);
            intent.putExtra("uid_user", uid_user);
            startActivity(intent);
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> {
            //Intent intent = new Intent(home_page.this, MainActivity.class);
            //startActivity(intent);
        });
    }

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