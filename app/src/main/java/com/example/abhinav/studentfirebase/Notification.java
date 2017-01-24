package com.example.abhinav.studentfirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    RecyclerView mRecycler, mRecyclerimages;
    ArrayList<Event> objlist = new ArrayList<Event>();
    ;
    Event event = null;
    StorageReference storageReference;
    Context context = this;
    List list;
    FirebaseRecyclerAdapter<Event, EventHol> adapter;
    FirebaseRecyclerAdapter<Event, ImageHol> adapterimages;
    DatabaseReference databaseReference;
   public String[] arrayImageUrl;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageReference = FirebaseStorage.getInstance().getReference();
        list=new ArrayList();
       // arrayImageUrl=new String[100];


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
         databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("ani", dataSnapshot.getChildrenCount() + "count");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    String date = child.child("date").getValue().toString();
                  //  String description = child.child("description").getValue().toString();
                    String title = child.child("title").getValue().toString();
                    //String images = child.child("noofimages").getValue().toString();
                 //   showNextFeeNotification(title);




                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        adapter = new FirebaseRecyclerAdapter<Event, EventHol>(Event.class, R.layout.fragment_single_event, EventHol.class, databaseReference) {


            @Override
            protected void populateViewHolder(EventHol viewHolder, Event model, int position) {
                Log.d("ani", model.gett() + model.getnoofimages() + model.getDescription() + "ti");
                viewHolder.mDescription.setText(model.getDescription());
                viewHolder.mTitle.setText(model.getTitle());
                viewHolder.mDate.setText(model.gett());
                Log.d("ani",model.getEventid()+model.getnoofimages()+"eventid");

                //child(model.getEventid()+"_"+(i+1))
                int noOfImages = Integer.parseInt(model.getnoofimages());
                if (noOfImages != 0) {
                   arrayImageUrl = new String[noOfImages+1];



                    NotificationImagesAdapter adapterEventImages = new NotificationImagesAdapter(context,model,arrayImageUrl);
                    viewHolder.recyclerEventImages.setAdapter(adapterEventImages);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    viewHolder.recyclerEventImages.setLayoutManager(manager);


                    //  final int finalI = i;
//                        if (i < noOfImages) {
//                            adapterimages = new FirebaseRecyclerAdapter<Event, ImageHol>(Event.class, R.layout.row_event_text, ImageHol.class,databaseReference.orderByChild("noofimages")) {
//
//
//                                @Override
//                                protected void populateViewHolder(final ImageHol viewHolder, Event model, int position) {
//                                    Log.d("ani",position+"pos");
//
//                                    storageReference.child("pic").child(model.getEventid() + "_" + (position + 1) + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            Glide.with(getApplicationContext()).load(uri).listener((new RequestListener<Uri, GlideDrawable>() {
//                                                @Override
//                                                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                                    return false;
//                                                }
//
//                                                @Override
//                                                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                                    viewHolder.spinner.setVisibility(View.GONE);
//                                                    return false;
//                                                }
//                                            }))
//                                                    .into(viewHolder.imageView);
//                                        }
//                                    });
//                                }
//                                //  Glide.with(getApplicationContext()).using(new FirebaseStorage()).load(storageReference).into(viewHolder.imageView);
//
//
//                            };
//                            viewHolder.recyclerEventImages.setAdapter(adapterimages);
//                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
//                            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                            viewHolder.recyclerEventImages.setLayoutManager(manager);
//                        }

//                    Log.d("ani",model.getnoofimages()+noOfImages);
                }else if (noOfImages == 0) {
                    viewHolder.recyclerEventImages.setVisibility(View.GONE);
                }

            }

        };


        mRecycler = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);
//        //Adapter mAdapter = new Adapter(this, mResults);
//       EventAdapter1 mAdapter = new EventAdapter1(this, mResults);
        mRecycler.setAdapter(adapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void showNextFeeNotification(String title) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.small_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(android.app.Notification.DEFAULT_ALL);
        notificationBuilder.setContentTitle("New Notification");

        //String formattedDate = MyUtils.getFormattedDate(fee.getDate());
        notificationBuilder.setContentText(title);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_fee);
        notificationBuilder.setLargeIcon(bm);
        notificationBuilder.setTicker("Notification");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, FeeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notificationBuilder.build());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Notification Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public static class EventHol extends RecyclerView.ViewHolder {
        TextView mTitle, mUsername, mNooflike;
        TextView mDescription;
        RecyclerView recyclerEventImages;
        ImageView mImage;
        TextView mDate;
        ImageView mUserdp, mLikes, mDeletepost;

        public EventHol(final View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.textViewHeading);
            mDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            mDate = (TextView) itemView.findViewById(R.id.textViewDate);
            recyclerEventImages = (RecyclerView) itemView.findViewById(R.id.recyclerEventImage);


        }
    }

    public static class ImageHol extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar spinner;

        public ImageHol(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.event_image);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBar2);


        }
    }

    //back button pressed
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent i = new Intent(Notification.this, ActivityMenu.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  startActivity(i);
        return true;
    }//onOptionsItemSelected

}
