package comnickdchee.github.a3am.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import comnickdchee.github.a3am.Activities.NewBookActivity;
import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Nicholas
 * MyBooksFragment extends Fragment
 * it overwrites onCreateView
 */
public class MyBooksFragment extends Fragment {

    private ArrayList<Book> BookList = new ArrayList<>();
    private BookRecyclerAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private Status curStatus;
    public Boolean filtered = false;
    private RecyclerView recyclerView;

    private TextView noDataView;

    ///new filter button added
    private FloatingActionButton filter;
    //list for filtering
    private ArrayList<Book> orderedList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Opens a fragment which will show the books
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fabAddBookButton);

        /**
         * Passes intent to the NewBook activity, using StartActivityForResult
         * to get back the results of an activity.
         */
        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(view.getContext(), NewBookActivity.class);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, 1);
        });

        BookList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = database.getReference().child("users").child(mAuth.getUid()).child("ownedBooks");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        noDataView = view.findViewById(R.id.noDataView);

        recyclerView.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.INVISIBLE);

        adapter = new BookRecyclerAdapter(getActivity(), orderedList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Pop up menu for book filtering
        //code for filtering application
        orderedList = new ArrayList<>();
        filter = view.findViewById(R.id.filterBtn);
        filter.setSoundEffectsEnabled(false);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filter);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Clear the sorted list before adding
                        orderedList.clear();

                        BookRecyclerAdapter updatedAdapter = new BookRecyclerAdapter(getActivity(), orderedList);

                        switch (menuItem.getItemId()) {
                            case R.id.item2:

                                curStatus = Status.Available;
                                filtered = true;
                                filterData(true);

                                return true;

                            case R.id.item3:

                                curStatus = Status.Borrowed;
                                filtered = true;
                                filterData(true);
                                return true;

                            case R.id.item4:

                                curStatus = Status.Requested;
                                filtered = true;
                                filterData(true);

                                return true;

                            case R.id.item5:

                                curStatus = Status.Accepted;
                                filtered = true;
                                filterData(true);

                                return true;

                            case R.id.item6:

                                filtered = false;
                                filterData(false);

                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        DatabaseReference bookRef = database.getReference().child("books");

        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderedList.clear();
                String user = FirebaseAuth.getInstance().getUid();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Book book = child.getValue(Book.class);
                    if (book != null){
                        if (book.getOwnerID().equals(user)) {
                            findBook(book.getBookID());
                        }
                    }
                }
                if (orderedList.size() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void filterData (Boolean filtered) {

        orderedList.clear();

        if (!filtered){
            orderedList.addAll(BookList);
        }

        for (Book orderedBook : BookList) {
            if (orderedBook.getStatus() == curStatus) {
                orderedList.add(orderedBook);
            }
        }

        if (orderedList.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            noDataView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.INVISIBLE);
        }

        BookRecyclerAdapter updatedAdapter = new BookRecyclerAdapter(getActivity(), orderedList);

        // Bind to adapter and show results
        recyclerView.setAdapter(updatedAdapter);
        updatedAdapter.notifyDataSetChanged();
    }


    //This is the function to get data for the books. We will use it to swap out data from the dummy data.
    public void findBook(final String key) {
        mAuth = FirebaseAuth.getInstance();
        BookList.clear();

        DatabaseReference ref = database.getReference().child("books");
        Log.d("TestDataBookDir", ref.toString());
        Log.d("TestDataBookDir", ref.getKey().toString());
        Log.d("TestDataBookDir", ref.toString());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(key);
                StorageReference profileImageRef =
                        FirebaseStorage.getInstance().getReference("shelf@gmail.com" + "/" + "dp" + ".jpg");
                Log.d("TestImageBook", profileImageRef.toString());

                Log.d("TestDataBook", dataSnapshot.child(key).getValue().toString());
                String bookID = dataSnapshot.child(key).child("bookID").getValue().toString();
                String author = dataSnapshot.child(key).child("author").getValue().toString();
                String isbn = dataSnapshot.child(key).child("isbn").getValue().toString();
                String title = dataSnapshot.child(key).child("title").getValue().toString();
                String status = dataSnapshot.child(key).child("status").getValue().toString();
                Book b1 = new Book(isbn, title, author);
                b1.setBookID(bookID);
                b1.setStatus(Status.valueOf(status));

                if (storageRef != null) {
                    b1.setImage(key);
                }
                BookList.add(b1);
                adapter.notifyDataSetChanged();

                filterData(filtered);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //BookList.clear();
        int i = BookList.size();
        String s = Integer.toString(i);
        Log.d("TestDataBook", s);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d("MyBooks", "Recyclerview notified");
            adapter.notifyDataSetChanged();
        }


    }
}

