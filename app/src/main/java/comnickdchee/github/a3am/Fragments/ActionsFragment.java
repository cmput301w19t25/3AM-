package comnickdchee.github.a3am.Fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ActionsTabAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

/**
 * A simple {@link Fragment} subclass.
 */

// BLA stands for Borrowed, Lending, Actions because they all share the same type of data

public class ActionsFragment extends Fragment {

    private static final String TAG = "ActionsFragment";
    private ArrayList<Book> data = new ArrayList<>();
    private FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mFireBaseDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private ArrayList<Book> requestedBooksList = new ArrayList<>();
    Backend backend = Backend.getBackendInstance();

    public ActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Use the backend singleton to load the requested books,
        // and then we add the requested books to the list to show on
        // the fragment's recycler view

        /**
         * TODO: I will mimic adding a book using the addBook method of the backend singleton,
         * which will be done first by pushing the user class to the "users" table. Then, we
         * navigate to this actions fragment and perform add book.
         */

        // Add the book using the user data and update the tables
//        Book testBook = new Book("12345", "Harry Potter", "JK Rowling", backend.getFirebaseUser().getUid());
//        backend.addBook(testBook);

        //Create and return the recyclerView
        ActionsTabAdapter adapter = new ActionsTabAdapter(getActivity(), requestedBooksList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        backend.getRequestedBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                requestedBooksList.clear();
                requestedBooksList.addAll(books);
                adapter.notifyDataSetChanged();
                Log.d("dapteraawas asdfasf", "onCallback: adapateradfasdfasd");
            }
        });

        Log.d(TAG, "onCreateView: Finished View");
        return view;

    }



}
