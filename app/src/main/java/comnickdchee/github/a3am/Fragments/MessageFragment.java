package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import comnickdchee.github.a3am.Activities.messageActivity;
import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Adapters.MessageAdapter;
import comnickdchee.github.a3am.Adapters.MessageRecyclerAdapter;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Chat;
import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

/**
 * @author Asma
 *  MessageFragment extends Fragment
 *  it overwrites onCreateView
 */
public class MessageFragment extends Fragment {
    DatabaseReference databaseReference;
    public ArrayList<String> usersChattedWith;
    private ArrayList<ChatBox> chatBoxes = new ArrayList<>();
    MessageRecyclerAdapter messageRecyclerAdapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    List<Chat> mChat;
    ArrayList<String> uIDs;
    String myid;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Opens a fragment which will show the messages

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        init();
        getActivity().setTitle("Messages");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }

    // TODO:Remove this init once we have proper data
    public void init() {
        /*
        User user1 = new User("SampleUser1","sampleEmail","XXXXX","XXXXXX");
        User user2 = new User("SampleUser2","sampleEmail","XXXXX","XXXXXX");

        ChatBox chatbox = new ChatBox(user1,user2);
        chatbox.sendMessage("Hello", user1,user2, new Date());
        chatbox.sendMessage("Hi", user1,user2, new Date());
        chatBoxes.add(chatbox);

        ChatBox chatbox2 = new ChatBox(user1,user2);
        chatbox2.sendMessage("Who are you?",user1,user2,new Date());
        chatbox2.sendMessage("Specifically waiting and passing my time, watching these haters not come with a rhymr" +
                "watching these sakas are mimicking gimmicking then they stary falling of one at a time",user1,user2, new Date());
        chatBoxes.add(chatbox2);
        */
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        myid = mAuth.getUid();
        mChat = new ArrayList<>();
        uIDs = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uIDs.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender() != null && chat.getReceiver() != null) {
                        if (chat.getReceiver().equals(myid) || chat.getSender().equals(myid)) {
//                        usersChattedWith.add(chat.getSender());
                            Log.d("Chat", chat.getReceiver());
                            if(!uIDs.contains(chat.getReceiver())) {
                                uIDs.add(chat.getReceiver());
                            }
                            if(!uIDs.contains(chat.getSender())) {
                                uIDs.add(chat.getSender());
                            }
//                        usersChattedWith.add(chat.getReceiver());
                        }
                    }
                    uIDs.remove(myid);
                    Log.d("UIDS",uIDs.toString());
//                    Log.d("Image", imgUrl);
                    messageRecyclerAdapter = new MessageRecyclerAdapter(getActivity(), uIDs);
                    recyclerView.setAdapter(messageRecyclerAdapter);
                    //messageAdapter = new MessageAdapter(messageActivity.this, mChat, imgUrl);
                    //recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
