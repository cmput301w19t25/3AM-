package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class RequestersAdapter extends RecyclerView.Adapter<RequestersAdapter.ViewHolder>{

    private ArrayList<User> requesters;
    private Context mContext;
    private Book currentBook;
    private Backend backend = Backend.getBackendInstance();


    /** Constructor for this class
     * Takes in a context and the array list of all the users it needs to show
     * And also a reference for the current book being looked at*/
    public RequestersAdapter(Context _context, ArrayList<User> _requesters, Book _currentBook) {
        this.mContext = _context;
        this.requesters = _requesters;
        this.currentBook = _currentBook;
    }

    /** This creates inflates the recycler with the view held in the view holder*/
    @NonNull
    @Override
    public RequestersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.requests_card, viewGroup, false);

        RequestersAdapter.ViewHolder holder = new RequestersAdapter.ViewHolder(view);
        return holder;
    }

    /** This function bind all the data to the view holder
     * i - contains the index
     * viewHolder is the view Holder which holds the view for a single card*/
    @Override
    public void onBindViewHolder(@NonNull RequestersAdapter.ViewHolder viewHolder, final int i) {

        // Loads teh username, email, phone number and teh image for a single card
        viewHolder.requesterName.setText(requesters.get(i).getUserName());
        viewHolder.requesterEmailText.setText(requesters.get(i).getEmail());
        viewHolder.requesterPhoneText.setText(requesters.get(i).getPhoneNumber());
        loadImageFromOwnerID(viewHolder.requesterImage,requesters.get(i).getUserID());

        // This is run when accept request is clicked. This goes through firebase and updates the
        // proper data to make the request accepted
        viewHolder.acceptRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(requesters.get(i).getUserID(), "IN RECYCLER USER ID: ");

                currentBook.setCurrentBorrowerID(requesters.get(i).getUserID());

                String receiverkey = requesters.get(i).getUserID().toString();
                String receivername = requesters.get(i).getUserName();
                String senderkey = currentBook.getOwnerID();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.put("from",mAuth.getCurrentUser().getUid());
                notificationData.put("type","request");

                reference.child("notificationAccepts").child(receiverkey).child(mAuth.getCurrentUser().getDisplayName()).push().setValue(notificationData);
                backend.acceptRequest(requesters.get(viewHolder.getAdapterPosition()), currentBook);

            }
        });

        // This is run when reject request is clicked. This goes through firebase and updates the
        // proper data to make the request reejcted
        viewHolder.rejectRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backend.rejectRequest(requesters.get(viewHolder.getAdapterPosition()), currentBook);
            }
        });
    }

    /** This loads the profile picture of user with userID of uID and places the image in the
     * load imageView */
    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(mContext).load(imgUrl).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });

    }

    /** This gets the size of the requester list */
    @Override
    public int getItemCount() {
        return requesters.size();
    }

    /** A view Holder to hold the views of each Individual Cards */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // views inside ViewHolder
        public ImageView requesterImage;
        public TextView requesterName;
        public TextView requesterPhoneText;
        public TextView requesterEmailText;
        public ImageView acceptRequestView;
        public ImageView rejectRequestView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set the views
            requesterImage = itemView.findViewById(R.id.ivRequesterPhoto);
            requesterName = itemView.findViewById(R.id.embeddedName);
            requesterPhoneText = itemView.findViewById(R.id.embeddedCardPhoneNumber);
            requesterEmailText = itemView.findViewById(R.id.embeddedCardEmail);


            acceptRequestView = itemView.findViewById(R.id.ivAcceptRequestButton);
            rejectRequestView = itemView.findViewById(R.id.ivRejectRequestButton);
        }

    }
}
