package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by ABHINAV on 18-05-2016.
 */
public class AdapterEvent2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater minflater;
    Context con;

    String[] arrayImageUrl;
    View view;
    Event event;


    public AdapterEvent2(Context context, Event event) {
        minflater = LayoutInflater.from(context);
        con = context;
        this.event = event;

        int noOfImages = Integer.parseInt(event.getnoofimages());
        arrayImageUrl = new String[noOfImages];

        for (int i = 0; i < noOfImages; i++) {
//            arrayImageUrl[i] = "http://studentaggregator.org/events/" + event.getId() + "_" + (i+1) + ".png";
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = minflater.inflate(R.layout.row_event_text, parent, false);
        return new EventHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            final EventHolder eventHolder = (EventHolder) holder;

            //Log.d("mylog","position="+position);
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
            })).crossFade().into(eventHolder.mImageView);
//            eventHolder.mImageView.setOnClickListener(new OnImageClickListener(event.getId(), position));
        }

    }

    @Override
    public int getItemCount() {
        return Integer.parseInt(event.getnoofimages());
    }


    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;
        ProgressBar spinner;

        public EventHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.event_image);
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
//            Intent i = new Intent(con, FullScreenViewActivity.class);
//            i.putExtra("position", position);
//            Log.d("mylog", "position" + position);
//            i.putExtra("Id", id);
//            //i.putExtra("con",con);
//            //i.putExtra("con",con);
//            con.startActivity(i);
        }
    }

}
