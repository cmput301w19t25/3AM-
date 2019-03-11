package comnickdchee.github.a3am;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Adapters.RecyclerViewAdapter;
import comnickdchee.github.a3am.Fragments.BorrowedFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;

public class MyBooksFragment extends Fragment {

    private ArrayList<Book> BookList;
    private BookRecyclerAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fabAddBookButton);

        /**
         * Passes intent to the NewBook activity, using StartActivityForResult
         * to get back the results of an activity.
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewBookActivity.class);

                // using this to get back the results of an intent
                // Source: https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                startActivityForResult(intent, 1);

            }
        });

        BookList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = database.getReference().child(mAuth.getUid()).child("BooksListID");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String key = child.getKey();
                    findBook(key);

                    Log.d("TestData",child.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Book b1 = new Book("ISBN","TITLE","AUTHOR");
        Book b12 = new Book("ISBN","TITLE","AUTHOR");

        BookList.add(b1);
        BookList.add(b12);

        User user1 = new User("nchee","nchee@mom.ca","China","1234556");

        b12.setCurrentBorrower(user1);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new BookRecyclerAdapter(getActivity(), BookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    //This is the function to get data for the books. We will use it to swap out data from the dummy data.
    public void findBook(final String key){
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = database.getReference().child("BooksList");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("TestDataBook",dataSnapshot.child(key).getValue().toString());
                    String author = dataSnapshot.child(key).child("author").getValue().toString();
                    String isbn = dataSnapshot.child(key).child("isbn").getValue().toString();
                    String title = dataSnapshot.child(key).child("title").getValue().toString();

                    Book b1 = new Book(isbn,title,author);
                    BookList.add(b1);
                    Log.d("TestDataBook",BookList.get(0).getAuthor());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Fired after Add Book Activity has finished.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Book book = (Book) data.getSerializableExtra("NewBook");
            BookList.add(book);
            adapter.notifyDataSetChanged();
        }
    }
}
