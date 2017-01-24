package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ServiceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVideo;
    private Context context = this;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Video,VideoHol> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("services");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter = new FirebaseRecyclerAdapter<Video,VideoHol>(Video.class, R.layout.fragment_video, VideoHol.class, dataSnapshot.getRef().child("videos")){

                    @Override
                    protected void populateViewHolder(final VideoHol viewHolder, Video model, int position) {
                        viewHolder.mTitle.setText(model.getTitle());
                        viewHolder.mDescription.setText(model.getDescription());
                        Log.d("ani",model.getDescription()+"video");
                        final String id = model.getId();
                        String imageurl = "http://img.youtube.com/vi/" + id + "/0.jpg"; //constant link to find thumbnamil
                        Uri uri = Uri.parse(imageurl);
                        Picasso.Builder builder = new Picasso.Builder(context);
                        builder.listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                                Picasso.with(context).load(R.drawable.error).networkPolicy(NetworkPolicy.OFFLINE)
                                        .into(viewHolder.mImage);
                            }
                        });
                        builder.build().load(uri).into(viewHolder.mImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                viewHolder.spinner.setVisibility(View.GONE);
                              //  Log.d("ani",model.getDescription()+"video");
                            }

                            @Override
                            public void onError() {
                                viewHolder.spinner.setVisibility(View.GONE);
                                Picasso.with(context).load(R.drawable.error).into(viewHolder.mImage);
                            }
                        });
                        viewHolder.linearLayoutVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(context, VideoPlayerActivity.class);
                                i.putExtra("id", id);
                                context.startActivity(i);
                            }
                        });



                    }

                };
                recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerViewVideo);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                recyclerViewVideo.setLayoutManager(manager);
                recyclerViewVideo.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //int id = item.getItemId();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return true;
    }
    public  static class VideoHol extends RecyclerView.ViewHolder {
        TextView mTitle,mId;
        TextView mDescription;
        LinearLayout linearLayoutVideo;
        RecyclerView recyclerresultdata;
ImageView mImage;
        ProgressBar spinner;
        TextView mUrl;


        public VideoHol(final View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            mDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            mImage= (ImageView) itemView.findViewById(R.id.imageViewVideo);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBarLoadImage);
            linearLayoutVideo = (LinearLayout) itemView.findViewById(R.id.linearLayoutVideo);



        }
    }
}
