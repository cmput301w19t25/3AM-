package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Adapters.MessageRecyclerAdapter;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

/**
 * @author Asma
 *  MessageFragment extends Fragment
 *  it overwrites onCreateView
 */
public class MessageFragment extends Fragment {

    private ArrayList<ChatBox> chatBoxes = new ArrayList<>();
    private MessageRecyclerAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Opens a fragment which will show the messages

        View view = inflater.inflate(R.layout.fragment_message, container, false);

        init();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new MessageRecyclerAdapter(getActivity(), chatBoxes);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }

    // TODO:Remove this init once we have proper data
    public void init() {
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
    }
}
