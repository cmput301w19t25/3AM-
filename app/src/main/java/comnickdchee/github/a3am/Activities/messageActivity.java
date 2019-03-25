package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

        //Retrieve Messenger's name.
        receiverKey = bundle.getString("key");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(receiverKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("userName").getValue().toString().trim();
                recieverUserName.setText(userName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Function that loads Image using key in ImageView..
        loadImageFromOwnerID(receiverImage,receiverKey);

        //Rig send and message.
        sendMessage = findViewById(R.id.button_chatbox_send);
        userMessage = findViewById(R.id.edittext_chatbox);

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

    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

}