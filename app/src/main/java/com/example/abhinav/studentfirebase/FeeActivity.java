package com.example.abhinav.studentfirebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FeeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String id;String description ;
    LinearLayout footer;
    private String studentid;
    Utility utility;
    TextView textViewDueDate;
    TextView textViewAmount;
    TextView textViewEmpty;
    FirebaseRecyclerAdapter<Fee, EventHol> adapter;
    DatabaseReference databaseReference,dbnextfeepay;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bindUIComponents();

        utility = new Utility();
        id = utility.readFrompreferences(getApplicationContext(), "id", "");
        Log.d("ani", id + "in fee");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("f");
        databaseReference.keepSynced(true);
        dbnextfeepay = FirebaseDatabase.getInstance().getReference().child("nextfeepayment");


//        localDB = new LocalDB(context);
//        dbHelper = new DBHelper(this);
// nextFee = localDB.getNextPayment(localDB.getStudent().getId());


        //  feeList = dbHelper.getAllFee(localDB.getStudent().getId());
//        if (feeList == null) {
//            textViewEmpty.setVisibility(View.VISIBLE);
//        } else {
//            feeAdapter = new FeeAdapter(context, feeList);
//            recyclerView.setAdapter(feeAdapter);
//        }
//        if (nextFee != null) {
//            footer.setVisibility(View.VISIBLE);
//            setFooterText();
//            checkNextPayment();
//        }
        dbnextfeepay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {
//                    Log.d("ani",id+"id in next");
//                    Log.d("ani","amount");
                    description = dataSnapshot.child(id).child("description").getValue().toString();
                    String dueamount = dataSnapshot.child(id).child("dueamount").getValue().toString();
                    Log.d("ani", dueamount + "amount");
                    String duedate = dataSnapshot.child(id).child("duedate").getValue().toString();
//                    Log.d("ani",dueamount+duedate+"nextfeedetails");
                    footer.setVisibility(View.VISIBLE);
                    setFooterText(duedate, dueamount);
                    checkNextPayment(duedate);
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setOnClickListeners();
        adapter = new FirebaseRecyclerAdapter<Fee, EventHol>(Fee.class, R.layout.fragment_fee, EventHol.class, databaseReference.child(id)) {

            int[] icons = {R.drawable.check, R.drawable.exclamation, R.drawable.exam, R.drawable.tour};

            @Override
            protected void populateViewHolder(EventHol viewHolder, final Fee model, int position) {
                if (id != null) {
                    viewHolder.mAmount.setText(model.getamount());
                    viewHolder.mDate.setText(model.getdate());
                    // Log.d("ani", model.getdescription()+model.gettype()+model.getdate() + "feedetails");
                    if (model.gettype().equalsIgnoreCase("basic")) {
                        viewHolder.mImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                        viewHolder.mImage.setImageResource(icons[0]);
                    } else if (model.gettype().equalsIgnoreCase("tour")) {
                        viewHolder.mImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                        viewHolder.mImage.setImageResource(icons[3]);
                    } else if (model.gettype().equalsIgnoreCase("fine")) {
                        viewHolder.mImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                        viewHolder.mImage.setImageResource(icons[1]);
                    } else if (model.gettype().equalsIgnoreCase("exam")) {
                        viewHolder.mImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                        viewHolder.mImage.setImageResource(icons[2]);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String desc=model.getdescription();
                            ShowAlertDialog(desc);
                        }
                    });

                }

            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


    void bindUIComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler_fee);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        footer = (LinearLayout) findViewById(R.id.footer);
        textViewDueDate = (TextView) findViewById(R.id.textViewDueDate);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return true;
    }

    private void setFooterText(String date,String amount) {
        String formattedDate = MyUtils.getFormattedDate(date);
        String dueDate = "Due Date : " + formattedDate;
        String dueAmount = "Due Amount : "+amount + " INR";
        Log.d("ani",dueAmount+dueDate+"foter");
        textViewDueDate.setText(dueDate);
        textViewAmount.setText(dueAmount);

    }

    private void checkNextPayment(String date) {
        String dueDate =date;

        GregorianCalendar inputDate = MyUtils.getGregorianCalendar(dueDate);
        //  add 7 days to input date
        //  7 days margin for next payment
        assert inputDate != null;
        inputDate.add(Calendar.DATE, 7);
        GregorianCalendar todaysDate = new GregorianCalendar();

        if (todaysDate.compareTo(inputDate) == -1) {
            MyUtils.logThis("-1");
            footer.setBackgroundColor(ContextCompat.getColor(FeeActivity.this, R.color.colorAbsent));
        }
    }

    private void ShowAlert(String message) {
        //Generate views to pass to AlertDialog.Builder and to set the text
        View dlg;
        TextView tvText;
        try {
            //Inflate the custom view
            LayoutInflater inflater = getLayoutInflater();
            dlg = inflater.inflate(R.layout.dialog_mono, (ViewGroup) findViewById(R.id.dlgView));
            tvText = (TextView) dlg.findViewById(R.id.dlgText);
        } catch (InflateException e) {
           dlg = tvText = new TextView(context);
        }
        //Set the text
        tvText.setText(message);
        //Build and show the dialog
        new AlertDialog.Builder(context)
                .setTitle("Next fee details")
                .setCancelable(true)
                .setIcon(R.drawable.notification_fee)
                .setPositiveButton("Later", null)
                .setNegativeButton("Pay now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(context, Notification.class);
                        startActivity(i);
                    }
                })
                .setView(dlg)
                .show();    //Builder method returns allow for method chaining
    }
    private void ShowAlertDialog(String description) {


        String[] desc = description.split("[,]");
        int size = desc.length;
        String dialogMessage = "";
        //int total = 0;
        for (int i = 0; i < size; i++) {
            String[] data = desc[i].split("[:]");
            String name = data[0].toUpperCase();
            String value = data[1].toUpperCase();

            name = String.format("%-20s", name);
            value = String.format("%-10s", value);

            dialogMessage += name + value + "\n";
        }
Log.d("ani",dialogMessage+"in dialog");
        //Generate views to pass to AlertDialog.Builder and to set the text
        View dlg;
        TextView tvText;
        try {
            //Inflate the custom view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = LayoutInflater.from(context);
            dlg = inflater.inflate(R.layout.dialog_mono, null);
            //dlg = inflater.inflate(R.layout.dialog_mono, (ViewGroup) findViewById(R.id.dlgView));
            tvText = (TextView) dlg.findViewById(R.id.dlgText);
        } catch (InflateException e) {
            dlg = tvText = new TextView(context);
        }
        //Set the text
        tvText.setText(dialogMessage);
        //Build and show the dialog
        new AlertDialog.Builder(context)
                .setTitle("Fee Details")
                .setCancelable(true)
                .setIcon(R.drawable.notification_fee)
                .setPositiveButton("OK", null)
                .setView(dlg)
                .show();    //Builder method returns allow for method chaining
    }


    public static class EventHol extends RecyclerView.ViewHolder {
        TextView mAmount, mUsername, mNooflike;
        TextView mDescription;
        RecyclerView recyclerEventImages;
        ImageView mImage;
        TextView mDate;
        ImageView mUserdp, mLikes, mDeletepost;

        public EventHol(final View itemView) {
            super(itemView);
            mAmount = (TextView) itemView.findViewById(R.id.textViewPaid);
            mImage = (ImageView) itemView.findViewById(R.id.imageViewPayment);
            mDate = (TextView) itemView.findViewById(R.id.textViewfeeDate);


        }
    }

    private void setOnClickListeners() {
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String description = nextFee.getDescription();
                String[] desc = description.split("[,]");

                String dialogMessage = "";
                //int total = 0;
                for (String aDesc : desc) {
                    String[] data = aDesc.split("[:]");
                    String name = data[0].toUpperCase();
                    String value = data[1].toUpperCase();

                    name = String.format("%-20s", name);
                    value = String.format("%-10s", value);

                    dialogMessage += name + value + "\n";
                }
                ShowAlert(dialogMessage);

                /*
                final Dialog dialog = new Dialog(new ContextThemeWrapper(con,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth));

                dialog.setContentView(R.layout.dialog_simple);
                dialog.setTitle("Fee Details");

                TextView textViewContent = (TextView) dialog.findViewById(R.id.textViewContent);

                String s = nextFee.getDescription();

                Log.d(LOG_TAG, "description is " + s);
                String[] desc = s.split("[,]");
                int size = desc.length;
                Log.d(LOG_TAG, "desc size = " + size);

                String dialogString = "";
                //int total = 0;
                for (int i = 0; i < size; i++) {
                    String[] data = desc[i].split("[:]");
                    String name = data[0];
                    String value = data[1];

                    dialogString += name + "\t\t\t\t\t\t\t" + value + "\n";

                    Log.d(LOG_TAG, "desc[" + i + "] = " + name + "=" + value);
                }
                textViewContent.setText(dialogString);


                Button buttonokay = (Button) dialog.findViewById(R.id.buttonCancel);
                Button buttonSend = (Button) dialog.findViewById(R.id.buttonSend);
                buttonSend.setText("Pay now");
                buttonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(ActivityFee.this, Payment.class);
                        startActivity(i);
                    }
                });
                buttonokay.setText("Ok");
                buttonokay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                */

            }
        });
    }
}


