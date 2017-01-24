package com.example.abhinav.studentfirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class AddStudentActivity extends AppCompatActivity {
    //DBHelper db;

    private Context context = this;
 //   private ServerHelper serverHelper;
    private static String DUMMY_NAME = "av@gmail.com";
    private static String DUMMY_CONTACT = "papa@123";

    // UI references
    private EditText editTextName;
    private EditText editTextContact;
    private Button buttonAddStudent,forgotpass;
    DatabaseReference databaseReference,dbuser,db;
    private TextView textViewDummyStudent;
    private Button buttonAboutUs,btnSignup;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    Boolean processed=false;


    //global values
    private String email;
    private String password;
    private String fcmid,id;
    Utility utility;
    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        utility=new Utility();
        Firebase.setAndroidContext(this);
        context = AddStudentActivity.this;
        auth = FirebaseAuth.getInstance();
        fcmid = FirebaseInstanceId.getInstance().getToken();
        bindUIComponents();
        setOnclickListeners();
        grantPermissions();

    }//onCreate

    private void grantPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                MyUtils.logThis("PERMISSION_GRANTED");
            } else {
                //Toast.makeText(context, "Permission required to progress further", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);
            }
        }
    }

    private void bindUIComponents() {
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        editTextName = (EditText) findViewById(R.id.editTextStudentName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        buttonAboutUs = (Button) findViewById(R.id.buttonAboutUs);
        textViewDummyStudent = (TextView) findViewById(R.id.textViewDummyStudent);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("student");
        db= FirebaseDatabase.getInstance().getReference();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        forgotpass= (Button) findViewById(R.id.btn_resetpassword);

    }//bindUIComponents

    public void setOnclickListeners() {

        editTextContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editTextAdd || id == EditorInfo.IME_NULL) {
                    attemptFetchStudent();
                    return true;
                }
                return false;
            }
        });

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (MyUtils.hasActiveInternetConnection(context)) {
                    attemptFetchStudent();
                } else {
                    Toast.makeText(context, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, WebsiteActivity.class);
//                startActivity(i);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CreateAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddStudentActivity.this,ForgotPassword.class);
                startActivity(intent);

            }
        });

        textViewDummyStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setText(DUMMY_NAME);
                editTextContact.setText(DUMMY_CONTACT);
            }
        });

    }//setOnclickListeners


    private void attemptFetchStudent() {

        //Reset errors
        editTextName.setError(null);
        editTextContact.setError(null);

        // Store values
        email = editTextName.getText().toString();
        password = editTextContact.getText().toString();

        //cancel will be set to true if there are form errors (invalid email, missing fields, etc.)
        //and field will be focused
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            editTextContact.setError(getString(R.string.error_field_required));
            focusView = editTextContact;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
//        else if (!isContactValid(password)) {
//            editTextContact.setError(getString(R.string.error_invalid_contact));
//            focusView = editTextContact;
//            cancel = true;
//        }

        // Check for a valid name.
        if (TextUtils.isEmpty(email)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        }

        // There was an error; don't attempt signup and focus the first form field with an error.
        if (cancel) {
            focusView.requestFocus();
        } else {

//if (email.contains(DUMMY_NAME)|| password.contains(DUMMY_CONTACT)){
//    Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
//    startActivity(intent);
//}
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.hasChild(email)) {
//                        String name = dataSnapshot.child(email).child("name").getValue().toString();
//                        Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
//                        startActivity(intent);
//                        databaseReference.removeEventListener(this);
//                        Log.d("ani", name);
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(),"Student desnt exist",Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(AddStudentActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                            //  Log.d("ani",auth.getCurrentUser().getProviders());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                   // inputPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                   /// task.getException();
                                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage()+"", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.d("ani",auth.getCurrentUser().getUid()+"uidlgin");
                                processed=true;
                                dbuser=FirebaseDatabase.getInstance().getReference().child("users");
                                dbuser.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (processed) {
                                            if (dataSnapshot.hasChild(auth.getCurrentUser().getUid().toString())) {
                                                id = dataSnapshot.child(auth.getCurrentUser().getUid().toString()).child("id").getValue().toString();
                                                utility.saveTopreferences(AddStudentActivity.this, id);
                                                processed = false;
                                                Intent intent=new Intent(AddStudentActivity.this,MenuActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                Log.d("ani", id + "id");
                                            }
                                            else {
                                                Toast.makeText(AddStudentActivity.this,"Please register first",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
//                                db.child("addstudents").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                   db.child("addstudents").child(auth.getCurrentUser().getUid().toString()).push().setValue("id","1");
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });

                                Log.d("ani", id + "id out");
                                progressBar.setVisibility(View.GONE);

                            }

                        }
                    });
        }}







    private boolean isContactValid(String contact) {
        return contact.length() == 10;
    }//isContactValid

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        progressDialog.dismiss();
    }

    //todo use this line to cancel the request
    //AppController.getInstance().cancelPendingRequests(tag_string_req);

}//AddStudent
