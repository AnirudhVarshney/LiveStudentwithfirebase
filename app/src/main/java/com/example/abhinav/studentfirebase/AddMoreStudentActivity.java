package com.example.abhinav.studentfirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class AddMoreStudentActivity extends AppCompatActivity {
    //DBHelper db;

    private Context context = this;


    // UI references
    private EditText editTextEmail;
    private EditText editTextID;
    private Button buttonAddStudent;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    DatabaseReference databaseReference,dbuser;
    //global values
    private String studentemail;
    FirebaseUser firebaseUser;
    private String id,uid,existingemail,existingname;
    private String fcmid;
    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";
    Boolean processed=false;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        auth = FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();
 firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        context = AddMoreStudentActivity.this;
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        fcmid = FirebaseInstanceId.getInstance().getToken();

        bindUIComponents();
        setOnclickListeners();

    }//onCreate


    private void bindUIComponents() {
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        editTextEmail = (EditText) findViewById(R.id.editTextStudentemail);
        editTextID = (EditText) findViewById(R.id.editTextstudentid);
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }//bindUIComponents

    public void setOnclickListeners() {

        editTextID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                if (MyUtils.hasActiveInternetConnection(context)) {
                    attemptFetchStudent();
                } else {
                    Toast.makeText(context, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
                }
            }
        });

    }//setOnclickListeners


    private void attemptFetchStudent() {

        //Reset errors
        editTextEmail.setError(null);
        editTextID.setError(null);

        // Store values
        studentemail = editTextEmail.getText().toString();
        id = editTextID.getText().toString();

        //cancel will be set to true if there are form errors (invalid email, missing fields, etc.)
        //and field will be focused
        boolean cancel = false;
        View focusView = null;

        // Check for a valid contact
        if (TextUtils.isEmpty(studentemail)) {
            editTextID.setError(getString(R.string.error_field_required));
            focusView = editTextID;
            cancel = true;
        }
        // Check for a valid contact, if the user entered one.
//        else if (!isContactValid(id)) {
//            editTextID.setError(getString(R.string.error_invalid_contact));
//            focusView = editTextID;
//            cancel = true;
//        }

        // Check for a valid name.
        if (TextUtils.isEmpty(studentemail)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        }

        // There was an error; don't attempt signup and focus the first form field with an error.
        if (cancel) {
            focusView.requestFocus();
        } else {
            processed=true;
databaseReference.child("usersstudents").addValueEventListener(new ValueEventListener() {


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (processed) {
            if (dataSnapshot.hasChild(uid)) {


                if (!dataSnapshot.child(uid).hasChild(id)) {

                    Log.d("ani", "inside");
                    databaseReference.child("student").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(id)) {

                                existingemail = dataSnapshot.child(id).child("email").getValue().toString();
                                existingname = dataSnapshot.child(id).child("name").getValue().toString();
                                Log.d("ani", existingemail + studentemail + "email");
                                if (existingemail.equals(studentemail)) {
                                    databaseReference.child("usersstudents").child(uid).child(id).child("name").setValue(existingname);
                                    Toast.makeText(AddMoreStudentActivity.this, "Student added succesfully", Toast.LENGTH_LONG).show();
                                    processed = false;
//                                    otp = generatePIN();
//                                   // String contact = dataSnapshot.child(id).child("contact").getValue().toString();
//                                    String contact="7009235305";
//
//                                    try {
//                                        sentRequest(otp, contact);
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
                                  //  }
                                    Intent intent = new Intent(AddMoreStudentActivity.this, MenuActivity.class);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(AddMoreStudentActivity.this, "Student id doesnt exists", Toast.LENGTH_LONG).show();
                                    processed = false;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Log.d("ani", existingemail + studentemail + "email");
                } else if (dataSnapshot.child(uid).hasChild(id)) {
                    Toast.makeText(AddMoreStudentActivity.this, "Student already exists with this id", Toast.LENGTH_LONG).show();
                    processed=false;
                }
            } else {
                Toast.makeText(AddMoreStudentActivity.this, "User not exists", Toast.LENGTH_LONG).show();
                processed=false;
            }
        }
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //sendNetworkRequest();
            //makeStringReq();
            //makeStringReq();
        }

    }//attemptFetchStudent



    private void sentRequest(final String otp, String contact) throws UnsupportedEncodingException {

        String url= null;
       // url="http://api.mVaayoo.com/mvaayooapi/MessageCompose?user=anirudhvarshney92@gmail.com:varshneyanir&senderID="+URLEncoder.encode("TEST SMS","UTF-8")+"&receipientno=9023606749&dcs=0&msgtxt=This is Test message&state=1";

        url = "http://api.mVaayoo.com/mvaayooapi/MessageCompose?user=anirudhvarshney92@gmail.com:varshneyanir&senderID="+ URLEncoder.encode("TEST SMS","UTF-8")+"&receipientno=9023606749&dcs=0&msgtxt="+URLEncoder.encode(otp,"UTF-8")+"&state=1";
 //url = "http://api.mVaayoo.com/mvaayooapi/MessageCompose?user=gauravgg06.gg@gmail.com:gauravgupta9&" +
       // "senderID="+ URLEncoder.encode("TEST SMS","UTF-8")+"&receipientno="+contact+"&dcs=0&msgtxt="+otp+"&state=1";
        Log.d("ani",contact+otp+"otp");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ani",response+"response");
                Toast.makeText(AddMoreStudentActivity.this, "Message Sent Successfully"+otp, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Applicationclass.getInstance().getRequestQueue().add(stringRequest);

    }

    public String generatePIN()
    {
        int x = (int)(Math.random() * 9);
        x = x + 1;
        String randomPIN = (x + "") + ( ((int)(Math.random()*1000)) + "" );
        return randomPIN;
    }


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
        progressDialog.dismiss();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(AddMoreStudentActivity.this, MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }

}//AddStudent
