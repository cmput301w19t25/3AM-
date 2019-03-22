package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comnickdchee.github.a3am.Activities.ViewOwnedBook;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private static final String TAG = "In_MessageRecyclerViewAdapter";
    FirebaseStorage storage;
    String DownloadLink;
    private ArrayList<ChatBox> mChatboxes;
    private Context mContext;

    public MessageRecyclerAdapter( Context mContext, ArrayList<ChatBox> Chatboxes) {
        this.mChatboxes = Chatboxes;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MessageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // Creates a view based on the mybooks_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooks_card,parent,false);
        MessageRecyclerAdapter.ViewHolder holder  = new MessageRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageRecyclerAdapter.ViewHolder holder, final int i) {
        // This function sets up the data in the cards

        Log.d(TAG, "onBindViewHolder: called.");
        ArrayList<User> users = mChatboxes.get(i).getUser();
        int userIndex = 0;

        // TODO: Change the case to contain current user's actual Username
        // Changes user index if user1 is the current user.
        if (users.get(0).getUserName().equals("Current User's Username")) {
            userIndex = 1;
        }

        holder.tvUsername.setText(users.get(userIndex).getUserName());
        holder.tvLastMessage.setText(mChatboxes.get(i).getLastMessage().getMessageText());

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // TODO: Correctly implement images for the other user's profile pic Below is the code used for Book Fragment's pictures by Zaheen
        /*
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(mChatboxes.get(i).getImage());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                Picasso.with(mContext).load(DownloadLink).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.ivBook);
            }
        });*/


        // On click event when a card is clicked
        holder.cvChatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mChatboxes.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatboxes.size();
    }


    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivUserPhoto;
        public TextView tvUsername;
        public TextView tvLastMessage;
        public CardView cvChatbox;


        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            cvChatbox = itemView.findViewById(R.id.cvChatbox);
        }
    }

}
