package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class RequestersAdapter extends RecyclerView.Adapter<RequestersAdapter.ViewHolder>{

    // private ArrayList<User> requesters;
    private ArrayList<User> requesters;   // TODO: Change this so that it take in users instead.
    private Context mContext;

    public RequestersAdapter(Context _context, ArrayList<User> _requesters) {
        this.mContext = _context;
        this.requesters = _requesters;
    }

    @NonNull
    @Override
    public RequestersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.requests_card, viewGroup, false);

        RequestersAdapter.ViewHolder holder = new RequestersAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestersAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.requesterName.setText(requesters.get(i).getUserName());

        // attach on click listener to each view of requesters' action buttons
        viewHolder.acceptRequestView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeRequesterAtPos(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesters.size();
    }

    /** Removes requester from the list and updates it. */
    private void removeRequesterAtPos(int pos) {
        requesters.remove(pos);
        notifyDataSetChanged();
    }

    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // views inside ViewHolder
        public ImageView requesterImage;
        public TextView requesterName;
        public RatingBar rating;
        public ImageView acceptRequestView;
        public ImageView rejectRequestView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set the views
            requesterImage = itemView.findViewById(R.id.ivRequesterPhoto);
            requesterName = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.ratingBar);
            acceptRequestView = itemView.findViewById(R.id.ivRejectRequestButton);
            rejectRequestView = itemView.findViewById(R.id.ivAcceptRequestButton);
        }
    }
}
