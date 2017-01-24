package com.example.abhinav.studentfirebase;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TaskActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String studentid;
    Context context;
    FirebaseRecyclerAdapter<Task,TaskHol> adapter;
    Utility utility;
    TextView textViewEmpty;
Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        utility = new Utility();
        studentid = utility.readFrompreferences(getApplicationContext(), "id", "");
context=this.getApplicationContext();
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              String studentclass=  dataSnapshot.child("student").child(studentid).child("class").getValue().toString();
                String div=dataSnapshot.child("student").child(studentid).child("div").getValue().toString();
                Log.d("ani",studentclass+div+"inside data");
                query=dataSnapshot.getRef().child("taskstudents").child(studentclass).orderByChild("division").equalTo(div);
                adapter = new FirebaseRecyclerAdapter<Task, TaskHol>(Task.class, R.layout.fragment_task, TaskHol.class, query) {

                    @Override
                    protected void populateViewHolder(TaskHol viewHolder, final Task model, int position) {
                        Log.d("ani",model.getDescription()+"inside task");
                        viewHolder.mDescription.setText(model.getDescription());
                        viewHolder.mTitle.setText(model.getTitle());
                        viewHolder.download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url=model.getUrl();
                                Log.d("ani",url+"url");
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setDescription("Downloading " + model.getFilename());
                                request.setTitle("Downloading " + model.getFilename());
                                // in order for this if to run, you must use the android 3.2 to compile your app
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                }
                                request.setDestinationInExternalPublicDir("studentaggregator", model.getFilename());

                                // get download service and enqueue file
                                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.enqueue(request);
                                Toast.makeText(context, "Download started", Toast.LENGTH_LONG).show();
                            }


                        });

                    }
                };
                recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





//        if (taskList == null) {
//            textViewEmpty.setVisibility(View.VISIBLE);
//            Log.d("mylog", "no records in task");
//        } else {
//            taskAdapter = new TaskAdapter(context, taskList);
//            recyclerView.setAdapter(taskAdapter);
//        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }
    public  static class TaskHol extends RecyclerView.ViewHolder {
        TextView mTitle, mDescription;
        Button download;

        public TaskHol(final View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            mDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            download= (Button) itemView.findViewById(R.id.buttonDownload);



        }
    }
}
