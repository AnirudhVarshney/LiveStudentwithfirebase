package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chat extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Users, Chat.EventHol> adapter;
    DatabaseReference databaseReference,dbnextfeepay;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        adapter = new FirebaseRecyclerAdapter<Users, Chat.EventHol>(Users.class, R.layout.fragment_chat_users, Chat.EventHol.class, databaseReference) {



            @Override
            protected void populateViewHolder(final Chat.EventHol viewHolder, final Users model, int position) {
                Log.d("ani",model.getEmail());
                   // viewHolder.mImage.setI(model.getamount());
                    viewHolder.mPhoneNo.setText(model.getPhone());
                    // Log.d("ani", model.getdescription()+model.gettype()+model.getdate() + "feedetails");
                Glide.with(getApplicationContext()).load(model.getImageurl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.mImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.mImage.setImageDrawable(circularBitmapDrawable);


                    }
                });



            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    public static class EventHol extends RecyclerView.ViewHolder {
        TextView mImageurl, mUsername, mPhoneNo;
        TextView mDescription;
        RecyclerView recyclerEventImages;
        ImageView mImage;
        TextView mDate;
        ImageView mUserdp, mLikes, mDeletepost;

        public EventHol(final View itemView) {
            super(itemView);
           mImage= (ImageView) itemView.findViewById(R.id.chatViewdp);

            mPhoneNo = (TextView) itemView.findViewById(R.id.chatViewphno);


        }
    }
}
