package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 20-05-2016.
 */

public class EventAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater minflater;
    ArrayList<Event> mResults;
    Context con;

    public EventAdapter1(Context context, ArrayList<Event> Results) {
        minflater = LayoutInflater.from(context);
        mResults = Results;
        con = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.fragment_single_event, parent, false);
        return new EventHol(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHol) {
            EventHol eventHolder = (EventHol) holder;
            Event event = mResults.get(position);
            eventHolder.textViewDate.setText(event.gett());
            eventHolder.textViewHeading.setText(event.getTitle());
            eventHolder.textViewDescription.setText(event.getDescription());

            int noofimages = Integer.parseInt(event.getnoofimages());
            if (noofimages != 0) {
                AdapterEvent2 mAdapter = new AdapterEvent2(con, event);
                eventHolder.recyclerEventImage.setAdapter(mAdapter);
                LinearLayoutManager manager = new LinearLayoutManager(minflater.getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                eventHolder.recyclerEventImage.setLayoutManager(manager);
            } else {
                eventHolder.recyclerEventImage.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mResults.size();
    }

    private class EventHol extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDate;
        TextView textViewHeading;
        TextView textViewDescription;
        RecyclerView recyclerEventImage;

        public EventHol(View itemView) {
            super(itemView);

            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewHeading = (TextView) itemView.findViewById(R.id.textViewHeading);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            recyclerEventImage = (RecyclerView) itemView.findViewById(R.id.recyclerEventImage);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
