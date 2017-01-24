package com.example.abhinav.studentfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputstudentid, username, userphoneno;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    ImageView profilepic;
    private FirebaseAuth auth;
    Utility utility;
    DatabaseReference db, db1;
    StorageReference mStorage;
    Uri prfileimageurl;
    String email,password,id,name,phno,existingname;
    Boolean processed=false;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        mStorage = FirebaseStorage.getInstance().getReference();
        utility = new Utility();
        auth = FirebaseAuth.getInstance();
        final Utility utility = new Utility();

        db = FirebaseDatabase.getInstance().getReference();
//profilepic= (ImageView) findViewById(R.id.profilpic);


        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputstudentid = (EditText) findViewById(R.id.studentid);
        username = (EditText) findViewById(R.id.username);
        userphoneno = (EditText) findViewById(R.id.userphno);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                 id = inputstudentid.getText().toString().trim();
                name=username.getText().toString().trim();
                phno=userphoneno.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(getApplicationContext(), "Enter Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty( name)) {
                    Toast.makeText(getApplicationContext(), "Enter Phonenumber", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phno)) {
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                processed = true;
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                       // Log.d("ani",dataSnapshot.child("student").child(id).child("email").getValue().toString()+"email"+email);
                        if (processed) {
                            if (dataSnapshot.child("student").hasChild(id)) {

                                if (dataSnapshot.child("student").child(id).child("email").getValue().toString().equals(email)) {
                                    auth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    Toast.makeText(CreateAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(CreateAccountActivity.this, "Authentication failed." + task.getException(),
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        final String uid = auth.getCurrentUser().getUid();
                                                        Log.d("ani", uid + "uid");
                                                        existingname=dataSnapshot.child("student").child(id).child("name").getValue().toString();
                                                        saveindatabase(uid);
                                                        processed=false;


                                                    }
                                                }
                                            });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter the registered email address", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    processed=false;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter the correct student id", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                processed=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void saveindatabase(final String uid) {
        db1=db.child("users").child(uid);
        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Log.d("ani",uid+name+phno+email+id+"in data");
                    Users newUser = new Users(name,phno,email,id);
                    db1.setValue(newUser);
                    progressBar.setVisibility(View.GONE);
                    processed=false;
//
                }
                Log.d("ani",existingname+"name inside");
                db.child("usersstudents").child(uid).child(id).child("name").setValue(existingname);
                utility.saveTopreferences(CreateAccountActivity.this,id);
                Intent intent = new Intent(CreateAccountActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateAccount Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_SELECT_PICTURE) {
//               Uri selectedUri = data.getData();
//                CropImage.activity(selectedUri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1,1).setAutoZoomEnabled(true)
//                        .start(this);}}
//                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    if (resultCode == RESULT_OK) {
//                        prfileimageurl = result.getUri();
//                        Log.d("ani",prfileimageurl+"uri in acc");
//                        Glide.with(this).load(prfileimageurl).centerCrop().into(profilepic);
//                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                        Exception error = result.getError();
//                        Log.d("ani",error+"errr");
//                    }
//                }
//
//                //Log.d("ani",selectedUri+"uri");
//            }

}

