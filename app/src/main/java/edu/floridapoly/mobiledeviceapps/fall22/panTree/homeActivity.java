package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class homeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ItemObject> items_list;
    Button AddItem;
    Button refreshbutton;
    EditText ItemName;
    ArrayAdapter<ItemObject> arrayAdapter;
    Button homeButton;
    Button sharesButton;
    Button logoutButton;
    private FirebaseAuth mAuth;
    String email_user;

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
            if (items.matches(""))
                return;

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
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Home page where you can view your pantry items. A quantity will later be added to each",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        refreshbutton = findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(view -> {
            items_list = new ArrayList<>();
            DocumentReference doc3Ref = db.collection("Access_codes").document(uid_user);
            System.out.println("Self UID: " + uid_user);

            doc3Ref.get().addOnCompleteListener(task -> {
                //to commit
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("UID data: "  + document.getData());
                        System.out.println("UID data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                        // get all the UIDs in the access code document
                        Collection<Object> uidCollection = document.getData().values();

                        // convert it to an arraylist
                        ArrayList<String> uidList = new ArrayList<>();
                        for(Object uidCollectionObject : uidCollection){
                            uidList.add(uidCollectionObject.toString());
                        }

                        // add self UID to the list since the user isn't (shouldn't) be inside their own share list
                        if(!uidList.contains(uid_user)) {
                            uidList.add(uid_user);
                        }

                        // loop over the objects in the collection and convert them to strings
                        // then add them to the arraylist
                        for(Object uid_object : uidList){
                            System.out.println("Access_codes UID item: " + uid_object.toString());

                            DocumentReference doc2Ref = db.collection("Lists").document(uid_object.toString());
                            doc2Ref.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document1 = task1.getResult();
                                    if (document1.exists()) {
                                        System.out.println("Item list data: "  + document1.getData());
//                                        System.out.println("Item list type: "  + Objects.requireNonNull(document1.getData()).getClass().getName());

                                        // get the values of all of the fields and convert to array for ease
                                        Collection<Object> items_collection_values = Objects.requireNonNull(document1.getData()).values();
                                        ArrayList<Object> items_collection = new ArrayList<>(items_collection_values);
                                        System.out.println("items_collection: " + items_collection);

                                        // get the field names and convert to array for ease
                                        Set<String> items_keys_set = document1.getData().keySet();
                                        ArrayList<String> items_keys = new ArrayList<>(items_keys_set);
                                        System.out.println("items_keys: " + items_keys);

                                        // loop over the objects in the collection
                                        // and create a new ItemObject with the values of:
                                        // - UID (this is the document name)
                                        // - Field_name (the name of the field for that item)
                                        // - Name (name of the item i.e "bananas")
                                        for(int i = 0; i < items_collection.size(); i++){
                                            items_list.add(new ItemObject(uid_object.toString(), items_keys.get(i), items_collection.get(i).toString()));
                                        }
                                        //update adapter
                                        Collections.sort(items_list, (i1, i2) -> {
                                            return i1.getName().compareTo(i2.getName());
                                        });
                                        System.out.println("Items list: " + items_list.toString());
                                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items_list);

                                        listView.setAdapter(arrayAdapter);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task1.getException());
                                }
                            });
                        }
                    }
                    else { // If the access code list does not exist, just get the users self list
                        System.out.println("Getting self list since access code list does not exist");
                        DocumentReference doc2Ref = db.collection("Lists").document(uid_user);
                        doc2Ref.get().addOnCompleteListener(selfTask -> {
                            if (selfTask.isSuccessful()) {
                                DocumentSnapshot selfListDocument = selfTask.getResult();
                                if (selfListDocument.exists()) {
                                    System.out.println("Self list data: "  + selfListDocument.getData());
//                                    System.out.println("Self list data type: "  + Objects.requireNonNull(selfListDocument.getData()).getClass().getName());
                                    // get the values of all of the fields and convert to array for ease
                                    Collection<Object> items_collection_values = Objects.requireNonNull(selfListDocument.getData()).values();
                                    ArrayList<Object> items_collection = new ArrayList<>(items_collection_values);
                                    System.out.println("Self items_collection: " + items_collection);

                                    // get the field names and convert to array for ease
                                    Set<String> items_keys_set = selfListDocument.getData().keySet();
                                    ArrayList<String> items_keys = new ArrayList<>(items_keys_set);
                                    System.out.println("Self items_keys: " + items_keys);
                                    // loop over the objects in the collection
                                    // and create a new ItemObject with the values of:
                                    // - UID (this is the document name)
                                    // - Field_name (the name of the field for that item)
                                    // - Name (name of the item i.e "bananas")
                                    for(int i = 0; i < items_collection.size(); i++){
                                        items_list.add(new ItemObject(uid_user, items_keys.get(i), items_collection.get(i).toString()));
                                    }

                                    // sort the list with custom comparator (could maybe update this, but it requires api ver 24 instead of 21)
                                    Collections.sort(items_list, (i1, i2) -> {
                                        return i1.getName().compareTo(i2.getName());
                                    });
                                    System.out.println("Self item list: " + homeActivity.this.items_list.toString());
                                    //update adapter
                                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, homeActivity.this.items_list);
                                    listView.setAdapter(arrayAdapter);
                                }
                            } else {
                                Log.d(TAG, "get failed with ", selfTask.getException());
                            }
                        });

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ItemObject item = (ItemObject) listView.getItemAtPosition(position);
            System.out.println("SELECTED ITEM NAME: " + item.getName());
            System.out.println("SELECTED ITEM FIELD_NAME: " + item.getField_name());
            System.out.println("SELECTED ITEM PARENT DOC: " + item.getParent_document());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete " + item.getName() + "?");
            builder.setMessage("Are you sure you want to delete '" + item.getName() + "'?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        System.out.println("Trigger delete event for item Field_name: " + item.getField_name());
                        DocumentReference docRef = db.collection("Lists").document(item.getParent_document());
                        // Remove the 'capital' field from the document
                        Map<String,Object> updates = new HashMap<>();
                        updates.put(item.getField_name(), FieldValue.delete());

                        docRef.update(updates).addOnCompleteListener(task -> {
                            System.out.println("ITEM DELETED WITH NAME: " + item.getName() + ", FIELD_NAME: " + item.getField_name());
                            refreshbutton.callOnClick();
                        });
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> System.out.println("Cancelled deletion event"));
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        sharesButton = findViewById(R.id.sharesButton);
        sharesButton.setOnClickListener(view -> {
            Intent intent = new Intent(homeActivity.this, sharesActivity.class);
            intent.putExtra("email", email_user);
            intent.putExtra("uid_user", uid_user);
            startActivity(intent);
        });

       // logoutButton = findViewById(R.id.logoutButton);
        //logoutButton.setOnClickListener(view -> {
            //Intent intent = new Intent(home_page.this, MainActivity.class);
            //startActivity(intent);
        //});
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