package com.example.abhinav.studentfirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Ray on 11-Jun-16.
 */
public class OTPVerificationActivity extends AppCompatActivity {



    private Context context = this;
    private String tag_string_req = "string_req";

    // UI references
    private EditText editTextCode;
    private Button buttonSubmit;
    private TextView textViewNumber;
    private ProgressDialog progressDialog;
    private String fcmid;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        bindUIComponents();
        setOnClickListeners();

        fcmid = FirebaseInstanceId.getInstance().getToken();
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);


    }//onCreate


    private void bindUIComponents() {
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
    }//bindUIComponents

    private void setOnClickListeners() {

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = editTextCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    editTextCode.setError(getString(R.string.error_field_required));
                    editTextCode.requestFocus();
                    return;
                }
                if (!isCodeValid(code)) {
                    editTextCode.setError(getString(R.string.error_invalid_code));
                    editTextCode.requestFocus();
                    return;
                }

                showProgressDialog();

                //sendNetworkRequest();
            }
        });
    }//setOnClickListeners

    /*
    public void sendNetworkRequest() {

        String URL = MyUtils.BASE_URL + "otp_verification.php";
        showProgressDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                MyUtils.logThis("response = " + response);
                hideProgressDialog();
                parseReply(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, getString(R.string.Oops), Toast.LENGTH_LONG).show();
                hideProgressDialog();
                MyUtils.logThis("Error: " + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid", student.getId());
                params.put("code", code);
                params.put("fcmid", fcmid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }//sendNetworkRequest
*/

    private boolean isCodeValid(String code) {
        return code.length() == 6;
    }//isCodeValid

    private void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }//showProgressDialog

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }//hideProgressDialog

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }//onOptionsItemSelected

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}
