package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;

public class NotificationImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
String path;
    String[] arrayImages;
    View view;
    Event event;
    StorageReference storageReference;
    int noOfImages;
    ArrayList<Uri> list;
    DatabaseReference databaseReference,db;
    Boolean processed=true;


    public NotificationImagesAdapter(Context context, Event event, String[] arrayImageUrl) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

        this.event = event;
        noOfImages = Integer.parseInt(event.getnoofimages());
        arrayImages = arrayImageUrl;

        list = new ArrayList();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        db=FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

    }


//        arrayImageUrl = new String[noOfImages];
//        String folder = context.getResources().getString(R.string.institute_folder);
//        for (int i = 0; i < noOfImages; i++) {
//            arrayImageUrl[i] = MyUtils.BASE_URL + "events/" + folder + "/" + event.getId() + "_" + (i + 1) + ".png";
//            //arrayImageUrl[i] = "http://www.studentaggregator.org/events/" + event.getId() + "_" + (i + 1) + ".png";
//            MyUtils.logThis(arrayImageUrl[i]);
//        }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_event_text, parent, false);
        return new EventHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof EventHolder) {
            final EventHolder eventHolder = (EventHolder) holder;
         //   processed = true;

            if (processed) {
;
             //  Uri uri=Uri.parse(path);
                //Log.d("ani",uri+"url");

                storageReference.child("pic").child(event.getEventid() + "_" + (position + 1) + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       list.add(uri);
                        processed=false;
                        Glide.with(context).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                eventHolder.spinner.setVisibility(View.GONE);
                                Log.d("ani",position+"pos");
                                processed=false;
                                return false;

                            }
                        }).into(eventHolder.imageView);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ani", e + "error");
                    }
                });


            }
        }


        }

    public class FirebaseImageLoader implements StreamModelLoader<StorageReference> {

        @Override
        public DataFetcher<InputStream> getResourceFetcher(StorageReference model, int width, int height) {
            return new FirebaseStorageFetcher(model);
        }

        private class FirebaseStorageFetcher implements DataFetcher<InputStream> {

            private StorageReference mRef;

            FirebaseStorageFetcher(StorageReference ref) {
                mRef = ref;
            }

            @Override
            public InputStream loadData(Priority priority) throws Exception {
                return Tasks.await(mRef.getStream()).getStream();
            }

            @Override
            public void cleanup() {
                // No cleanup possible, Task does not expose cancellation
            }

            @Override
            public String getId() {
                return mRef.getPath();
            }

            @Override
            public void cancel() {
                // No cancellation possible, Task does not expose cancellation
            }
        }
    }




/*
            Glide.with(con).load(arrayImageUrl[position]).centerCrop().listener((new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    eventHolder.spinner.setVisibility(View.GONE);
                    return false;
                }
            })).crossFade().into(eventHolder.imageView);
            eventHolder.imageView.setOnClickListener(new OnImageClickListener(event.getId(), position));
*/
//            MyUtils.logThis(arrayImageUrl[position]);
//            Uri uri = Uri.parse(arrayImageUrl[position]);
//
//            Picasso.Builder builder = new Picasso.Builder(context);
//            builder.listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    exception.printStackTrace();
//                    Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
//                    eventHolder.spinner.setVisibility(View.GONE);
//                }
//            });
//            builder.build().load(arrayImageUrl[position]).into(eventHolder.imageView, new Callback() {
//                @Override
//                public void onSuccess() {
//                    eventHolder.spinner.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
//                    eventHolder.spinner.setVisibility(View.GONE);
//                }
//            });
//
//            //set Click Listener on this image
//            eventHolder.imageView.setOnClickListener(new OnImageClickListener(event.getId(), position));

            /*Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).
                    into(eventHolder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(R.drawable.error).transform(new CircleTransform()).into(eventHolder.imageView);
                            eventHolder.spinner.setVisibility(View.GONE);
                        }
                    });
        */




    @Override
    public int getItemCount() {
        Log.d("ani",list.size()+"size");

        return  Integer.parseInt(event.getnoofimages());
    }

    public void  captureimage(Uri uri, final EventHolder eventHolder) {
        Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener((new RequestListener<Uri, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                eventHolder.spinner.setVisibility(View.GONE);
                return false;
            }
        }))
                .into(eventHolder.imageView);

    }
    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar spinner;

        public EventHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.event_image);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBar2);
        }

        @Override
        public void onClick(View v) {
        }

    }

    class OnImageClickListener implements View.OnClickListener {
        int position;
        String id;

        // constructor
        public OnImageClickListener(String id, int position) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Log.d("mylog", "AdapterEventImages - image position = " + position);

//            Intent i = new Intent(context, FullscreenImageActivity.class);
//            i.putExtra("Id", id);
//            i.putExtra("position", position);
//            i.putExtra("noOfImages", "" + noOfImages);
//            context.startActivity(i);
        }
    }

}
