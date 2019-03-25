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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import comnickdchee.github.a3am.Activities.ViewOwnedBook;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Chat;
import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final String TAG = "In_MessageAdapter";
    FirebaseStorage storage;

    private List<Chat>  mChat;
    private Context mContext;
    private String imgUrl;


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser fuser;

    public MessageAdapter( Context mContext, List<Chat> mChat, String imgUrl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imgUrl = imgUrl;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Creates a view based on the mybooks_card.xml
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, final int position) {
        // This function sets up the data in the cards

        Log.d(TAG, "onBindViewHolderMessage: called.");
        // TODO: Correctly implement images for the other user's profile pic Below is the code used for Book Fragment's pictures by Zaheen
        Log.d("Image", imgUrl);
        Chat chat = mChat.get(position);
        holder.showMessage.setText(chat.getMessage());

        if(imgUrl != null){
            Picasso.with(mContext).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.profilePhoto);
        }else{
            Picasso.with(mContext).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.profilePhoto);
        }

        // On click event when a card is clicked

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profilePhoto;
        public TextView showMessage;


        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_image);
            showMessage = itemView.findViewById(R.id.showMessage);

        }
    }

    @Override
    public int getItemViewType(int position){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

}
