package edu.floridapoly.mobiledeviceapps.fall22.panTree;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class sharesActivity extends AppCompatActivity {
    Button sharesButton;
    Button homeButton;
    Button settingsButton;
    EditText accesscodetext;
    Button refreshsharebutton;
    Button adduserbutton;
    ArrayList<AccessCodeObject> accessCodesList;
    TextView accesscode;
    private FirebaseAuth mAuth;
    String email_user;
    String user_uid;
    ListView listView;
    ArrayAdapter<AccessCodeObject> arrayAdapter;

    @SuppressLint("SetTextI18n")
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

        accesscode.setText("Access Code: " + user_uid);

        accesscode.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Access Code", user_uid);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(sharesActivity.this, "Access code copied to clipboard.", Toast.LENGTH_SHORT).show();
            return true;
        });

        refreshsharebutton.setOnClickListener(view -> {
            String accessCodeSelf = user_uid.toString();
            System.out.println("Self code: "  + accessCodeSelf);
            DocumentReference doc2Ref = db.collection("Access_codes").document(accessCodeSelf);
            doc2Ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("success1");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("success2");
                        System.out.println("Access_codes data: "  + document.getData());

                        System.out.println("Access_codes data type: "  + Objects.requireNonNull(document.getData()).getClass().getName());
                        // get the values of all of the fields and convert to array for ease
                        Collection<Object> codes_collection_values = document.getData().values();
                        ArrayList<Object> codes_collection = new ArrayList<>(codes_collection_values);
                        System.out.println("codes_collection: " + codes_collection);

                        // get the field names and convert to array for ease
                        Set<String> codes_keys_set = document.getData().keySet();
                        ArrayList<String> codes_keys = new ArrayList<>(codes_keys_set);
                        System.out.println("items_keys: " + codes_keys);
                        // create the string arraylist
                        accessCodesList = new ArrayList<>();

                        // loop over the objects in the collection and convert them to strings
                        // then add them to the arraylist
                        for(int i = 0; i < codes_collection.size(); i++){

                            System.out.println("CODE NAME: " + codes_collection.get(i).toString());
                            DocumentReference doc3Ref = db.collection("Users").document(codes_collection.get(i).toString());
                            int finalI = i;
                            doc3Ref.get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            DocumentSnapshot document1 = task2.getResult();
                                            if (document1.exists()) {
                                                System.out.println("EMAIL OF CODE: " + document1.getString("email"));
                                                accessCodesList.add(new AccessCodeObject(codes_keys.get(finalI), codes_collection.get(finalI).toString(), document1.getString("email")));
                                                arrayAdapter = new ArrayAdapter<>(sharesActivity.this, R.layout.custom_list_item, accessCodesList);
                                                listView.setAdapter(arrayAdapter);
                                            }
                                        }
                                        else {
                                            Log.d(TAG, "get failed with ", task2.getException());
                                        }
                            });
                        }
                        System.out.println("Access_codes list: " + accessCodesList.toString());
                        //update adapter

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        });
        adduserbutton.setOnClickListener(view -> {
            String accessCodeValue = accesscodetext.getText().toString();
            String accessCodeSelf = user_uid.toString();

            // if the access code is empty / blank
            if (accessCodeValue.matches(""))
                return;


            if(accessCodesList != null){
                System.out.println("accessCodesList: " + accessCodesList.toString());

                for(AccessCodeObject accessCodeObject : accessCodesList){
                    if(accessCodeValue.equals(accessCodeObject.getCode())){
                        accesscodetext.setText("");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "This user has already been added to your list of access codes.",
                                Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
            }


            // check if the document is valid
            Task<QuerySnapshot> colRef = db.collection("Lists").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean found = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(accessCodeValue.equals(document.getId())){
                            //to commit
                            DocumentReference doc2Ref = db.collection("Access_codes").document(accessCodeSelf);
                            doc2Ref.get().addOnCompleteListener(taskAddCode -> {
                                if (taskAddCode.isSuccessful()) {
                                    DocumentSnapshot documentAddCode = taskAddCode.getResult();
                                    if (documentAddCode.exists()) {
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
                                    Log.d(TAG, "get failed with ", taskAddCode.getException());

                                }
                            });
                            accesscodetext.setText("");
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Code is not valid.",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AccessCodeObject code = (AccessCodeObject) listView.getItemAtPosition(position);
            System.out.println("SELECTED SHARE CODE: " + code.getCode());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete share code?");
            builder.setMessage("Are you sure you want to delete '" + code + "'?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        System.out.println("Trigger delete event for code Field_name: " + code.getField_name());
                        DocumentReference docRef = db.collection("Access_codes").document(user_uid);

                        Map<String,Object> updates = new HashMap<>();

                        updates.put(code.getField_name(), FieldValue.delete());

                        docRef.update(updates).addOnCompleteListener(task -> {
                            System.out.println("SHARE CODE DELETED WITH VALUE: " + code.getCode());
                            refreshsharebutton.callOnClick();
                        });
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> System.out.println("Cancelled deletion event"));
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(sharesActivity.this, homeActivity.class);
            startActivity(intent);
        });

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(sharesActivity.this, settingActivity.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("uid_user", extras.getString("uid_user"));
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