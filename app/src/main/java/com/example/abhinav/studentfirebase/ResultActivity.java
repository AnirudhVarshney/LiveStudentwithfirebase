package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity {
    String error;
    RecyclerView mRecycler;
    String[] descrip;
    Context context = this;
    TextView textViewEmpty;
    int l;
    private String studentid,score;
    Utility utility;
    FirebaseRecyclerAdapter<StudentResult,ResultHol> adapter;
    FirebaseRecyclerAdapter<StudentResult,ResultdataHol> adapterresult;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        utility = new Utility();
        studentid = utility.readFrompreferences(getApplicationContext(), "id", "");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("results");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(studentid)){
//
                    Log.d("ani",studentid+"stid");
                    adapter = new FirebaseRecyclerAdapter<StudentResult,ResultHol>(StudentResult.class, R.layout.fragment_result, ResultHol.class, dataSnapshot.getRef().child(studentid).child("2016-2017")){
                        @Override
                        protected void populateViewHolder(ResultHol viewHolder, StudentResult model, int position) {
                            Log.d("ani",model.getDescription()+model.getData()+model.getDate()+model.getTitle()+"result desc");
                            score=model.getData();
                            viewHolder.mDate.setText(model.getDate());
                            if(!model.getData().isEmpty()){
                                ResultScoreAdapter mAdapter = new ResultScoreAdapter(getApplicationContext(),score);
                                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                viewHolder.recyclerresultdata.setLayoutManager(manager);
                                viewHolder.recyclerresultdata.setAdapter(mAdapter);
                            }
                        }
                    };
                    mRecycler = (RecyclerView) findViewById(R.id.rv_result);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    mRecycler.setLayoutManager(manager);
                    mRecycler.setAdapter(adapter);

                }
                else {
                        textViewEmpty.setVisibility(View.VISIBLE);
              }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//

    }


    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(context, MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }//onOptionsItemSelected
    public  static class ResultHol extends RecyclerView.ViewHolder {
        TextView mTitle,mNoitems;
        TextView mDescription;
        RecyclerView recyclerresultdata;

        TextView mDate;


        public ResultHol(final View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.resulttitle);
            mDescription = (TextView) itemView.findViewById(R.id.resultDesc);
            mDate = (TextView) itemView.findViewById(R.id.resultdate);
            recyclerresultdata = (RecyclerView) itemView.findViewById(R.id.rv_resultscores);


        }
    }
    public  static class ResultdataHol extends RecyclerView.ViewHolder {
        TextView mSubject, mScore;

        public ResultdataHol(final View itemView) {
            super(itemView);
            mSubject = (TextView) itemView.findViewById(R.id.subject);
            mScore = (TextView) itemView.findViewById(R.id.score);



        }
    }
}

