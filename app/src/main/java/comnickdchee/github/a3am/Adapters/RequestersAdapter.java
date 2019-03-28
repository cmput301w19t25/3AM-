package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class RequestersAdapter extends RecyclerView.Adapter<RequestersAdapter.ViewHolder>{

    // private ArrayList<User> requesters;
    private ArrayList<User> requesters;   // TODO: Change this so that it take in users instead.
    private Context mContext;
    private Book currentBook;
    private Backend backend = Backend.getBackendInstance();

    public RequestersAdapter(Context _context, ArrayList<User> _requesters, Book _currentBook) {
        this.mContext = _context;
        this.requesters = _requesters;
        this.currentBook = _currentBook;
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
        loadImageFromOwnerID(viewHolder.requesterImage,requesters.get(i).getUserID());

        // Accept request.
        viewHolder.acceptRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(requesters.get(i).getUserID(), "IN RECYCLER USER ID: ");
                backend.acceptRequest(requesters.get(i), currentBook);
            }
        });

        // Reject request.
        viewHolder.rejectRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backend.rejectRequest(requesters.get(viewHolder.getAdapterPosition()), currentBook);
            }
        });
    }

    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(mContext).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

    @Override
    public int getItemCount() {
        return requesters.size();
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
            acceptRequestView = itemView.findViewById(R.id.ivAcceptRequestButton);
            rejectRequestView = itemView.findViewById(R.id.ivRejectRequestButton);
        }

    }
}
