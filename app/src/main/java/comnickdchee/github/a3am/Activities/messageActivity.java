package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comnickdchee.github.a3am.Adapters.MessageAdapter;
import comnickdchee.github.a3am.Models.Chat;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class messageActivity extends AppCompatActivity {
    CircleImageView receiverImage;
    TextView recieverUserName;

    String userName;
    String receiverKey;
    public static String imgUrl;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Button sendMessage;
    EditText userMessage;


    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        //Retrieve and display messaging data.
        receiverImage = findViewById(R.id.messagingToImage);
        recieverUserName = findViewById(R.id.messagingTo);
        Bundle bundle = getIntent().getExtras();
        receiverKey = bundle.getString("key");
        //Function that loads Image using key in ImageView..
        loadImageFromOwnerID(receiverImage,receiverKey);

        //Retrieve Messenger's name.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(receiverKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("userName").getValue().toString().trim();
                recieverUserName.setText(userName);
                //Log.d("Image","Donkey"+imgUrl);
                imgUrl = bundle.getString("imgUrl");
                readMessages(firebaseUser.getUid().toString(),receiverKey,imgUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Load previous messages in Custom Messenger;
        recyclerView = findViewById(R.id.reyclerview_message_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        //Rig send and message.
        sendMessage = findViewById(R.id.button_chatbox_send);
        userMessage = findViewById(R.id.edittext_chatbox);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = userMessage.getText().toString();
                if(message == null){
                    Toast.makeText(messageActivity.this, "Cannot send empty messages!", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(message);
                }
                userMessage.setText("");
            }
        });

    }

    //Function that sends message to "Receiver" from Current User.
    private void sendMessage(String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", mAuth.getCurrentUser().getUid().toString());
        hashMap.put("receiver", receiverKey);
        hashMap.put("message", message);

        reference.child("chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, String userID, String imgUrl){
        mChat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userID) || chat.getReceiver().equals(userID) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
//                    Log.d("Image", imgUrl);
                    messageAdapter = new MessageAdapter(messageActivity.this, mChat, imgUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                imgUrl = uri.toString();

                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

}