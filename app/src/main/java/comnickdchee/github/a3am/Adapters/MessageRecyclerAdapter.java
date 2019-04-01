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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

import comnickdchee.github.a3am.Activities.ViewOwnedBook;
import comnickdchee.github.a3am.Activities.messageActivity;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;
/*
 * @author Zaheen
 *  MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>
 *  it overwrites onCreateView
 *  Used to view the cards/people you have messages with.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {

    private static final String TAG = "In_MessageRecyclerViewAdapter";
    FirebaseStorage storage;
    String DownloadLink;
    private ArrayList<ChatBox> mChatboxes;
    private Context mContext;
    private ArrayList<String> uIDS;
    String imgUrl;
    private static final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();


    public MessageRecyclerAdapter( Context mContext, ArrayList<String> uIDS) {
        this.uIDS = uIDS;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MessageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // Creates a view based on the mybooks_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbox_card,parent,false);
        MessageRecyclerAdapter.ViewHolder holder  = new MessageRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageRecyclerAdapter.ViewHolder holder, final int i) {
        // This function sets up the data in the cards

        setUserNameFromUid(uIDS.get(i),holder.tvUsername);
        loadImageFromOwnerID(uIDS.get(i), holder.ivUserPhoto);

        // On click event when a card is clicked
        holder.cvChatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + uIDS.get(i));
                Intent intent = new Intent (mContext, messageActivity.class);
                intent.putExtra("key", uIDS.get(i));
                intent.putExtra("imgUrl", imgUrl);
                //Log.d("keyIn",key);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uIDS.size();
    }


    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView ivUserPhoto;
        public TextView tvUsername;
        public TextView tvLastMessage;
        public CardView cvChatbox;


        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            //tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            cvChatbox = itemView.findViewById(R.id.cvChatbox);
        }
    }


    public void setUserNameFromUid(String UID, TextView t){
        DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
        usersRef = usersRef.child(UID).child("userName");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Messaging Tab", dataSnapshot.getValue().toString());
                t.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadImageFromOwnerID( String uID, ImageView load){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgUrl = uri.toString();
                Picasso.with(mContext).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

}
