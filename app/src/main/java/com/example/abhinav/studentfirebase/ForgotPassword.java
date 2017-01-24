package com.example.abhinav.studentfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {
    private EditText editTextID;
    private Button buttonSubmit;
    String id,email;
    private FirebaseAuth auth;
    DatabaseReference db;
    private TextView textViewNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bindUIComponents();
        setOnClickListeners();
    }
    private void bindUIComponents() {
        editTextID = (EditText) findViewById(R.id.editTextId);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        progressDialog=new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference().child("student");

    }//bindUIComponents

    private void setOnClickListeners() {

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
progressDialog.setMessage("Sending...");
                progressDialog.show();
                id = editTextID.getText().toString();
                if (TextUtils.isEmpty(id)) {
                    editTextID.setError(getString(R.string.error_field_required));
                    editTextID.requestFocus();
                    return;
                }
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                   if(dataSnapshot.hasChild(id)){
                       email=dataSnapshot.child(id).child("email").getValue().toString();
                       auth.sendPasswordResetEmail(email)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                       progressDialog.dismiss();
                                       Toast.makeText(ForgotPassword.this,"Password Reset link is sent to registered email",Toast.LENGTH_LONG).show();
                                       Log.d("ani","emailsent");
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(ForgotPassword.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();
                               Intent intent=new Intent(ForgotPassword.this,AddStudentActivity.class);
                               startActivity(intent);
                           }

                       });
                   }
                        else if(!dataSnapshot.hasChild(id)){
                       editTextID.setError("Enter correct id");
                       progressDialog.dismiss();
                       editTextID.requestFocus();
                   }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                if (!isCodeValid(code)) {
//                    editTextCode.setError(getString(R.string.error_invalid_code));
//                    editTextCode.requestFocus();
//                    return;
//                }

                //showProgressDialog();

                //sendNetworkRequest();
            }
        });
    }//setOnClickListeners
}
