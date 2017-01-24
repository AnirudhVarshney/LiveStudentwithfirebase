package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StudentInfoActivity extends AppCompatActivity {

    TextView textViewFathersName;
    TextView textViewMothersName;
    TextView textViewContactNumber;
    TextView textViewDateOfBirth;

    Context context = this;
    DatabaseReference databaseReference;
    Spinner spinner;
    TextView textViewTime;
    TextView textViewSubject;
    TextView textViewSubjectList;
    Utility utility;
    String studentid;
    String studentclass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindUIComponents();
        showData();

        setupTimetable();

    }



    private void setupTimetable() {

        final List<String> list = new ArrayList<>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                MyUtils.logThis("position = " + position);

                String time = "";
                String subject = "";
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentclass=  dataSnapshot.child("student").child(studentid).child("class").getValue().toString();
                        String div=dataSnapshot.child("student").child(studentid).child("div").getValue().toString();
                        Log.d("ani",div+studentclass+list.get(position));
                        if (dataSnapshot.child("timetable").hasChild(studentclass)){
                            if (dataSnapshot.child("timetable").child(studentclass).hasChild(div)) {
                                if (dataSnapshot.child("timetable").child(studentclass).child(div).hasChild(list.get(position))) {
                                    String data = dataSnapshot.child("timetable").child(studentclass).child(div).child(list.get(position)).child("data").getValue().toString();

                                    Log.d("anii", data + "data");
                                    String[] descrip = data.split("[;]");
                                    String t = "";
                                    String sub = "";
                                    ArrayList<String> time = new ArrayList<>();
                                    ArrayList<String> subject = new ArrayList<>();
                                    for (String aDescrip : descrip) {
                                        String[] details = aDescrip.split("[,]");
                                        String s1 = details[0];
                                        String s2 = details[1];
                                        time.add(s1);
                                        subject.add(s2);
                                    }
                                    for (int i = 0; i < time.size(); i++) {
                                        Log.d("ani", time.get(i));
                                        String showTime = time.get(i);
                                        String s = subject.get(i);
                                        t = t + showTime + "\n";
                                        sub = sub + s + "\n";
                                    }
                                    textViewTime.setText(t);
                                    textViewSubject.setText(sub);

                                }
                                else {
                                    textViewTime.setText("");
                                    textViewSubject.setText("");
                                }
                                String d=dataSnapshot.child("Subjects").child(studentclass).child(div).child("sub").getValue().toString();
                                String[] allsubjects = d.split("[,]");
                                String s="";
                                for (int i=0;i<allsubjects.length;i++){
                                    s=s+allsubjects[i]+"\n";

                                }
                                textViewSubjectList.setText(s);
                            }
                }


            }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                if (timetableList != null) {
//                    for (int i = 0; i < timetableList.size(); i++) {
//                        String startTimeLocal = timetableList.get(i).getStartTime();
//                        int startHour = Integer.parseInt(startTimeLocal.substring(0, 2));
//                        String startampm = "am";
//                        if (startHour > 12) {
//                            startampm = "pm";
//                        }
//                        String startTimeTrim = startTimeLocal.substring(0, 5);
//                        String endTimeLocal = timetableList.get(i).getEndTime();
//                        String endTimeTrim = endTimeLocal.substring(0, 5);
//
//                        String showTime = startTimeTrim + " - " + endTimeTrim + " " + startampm;
//                        time += showTime + "\n";
//                        subject += timetableList.get(i).getSubject() + "\n";
//                    }
               // }
             //   MyUtils.logThis("time=" + time);
               // MyUtils.logThis("subject=" + subject);
//                textViewTime.setText(time);
  //              textViewSubject.setText(subject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void bindUIComponents() {
        textViewFathersName = (TextView) findViewById(R.id.textViewFathersName);
        textViewMothersName = (TextView) findViewById(R.id.textViewMothersName);
        textViewContactNumber = (TextView) findViewById(R.id.textViewContactNumber);
        textViewDateOfBirth = (TextView) findViewById(R.id.textViewDateOfBirth);
        utility = new Utility();
        studentid = utility.readFrompreferences(getApplicationContext(), "id", "");
        spinner = (Spinner) findViewById(R.id.spinner);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        textViewSubjectList = (TextView) findViewById(R.id.textViewSubjectList);
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }//bindUIComponents

    private void showData() {
        databaseReference.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(studentid)) {
                    textViewFathersName.setText(dataSnapshot.child(studentid).child("father").getValue().toString());
                    textViewMothersName.setText(dataSnapshot.child(studentid).child("mother").getValue().toString());
                    textViewContactNumber.setText(dataSnapshot.child(studentid).child("contact").getValue().toString());
                    textViewDateOfBirth.setText(dataSnapshot.child(studentid).child("dob").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(context, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected
}
