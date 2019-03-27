package comnickdchee.github.a3am.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

    private ArrayList<Book> BookList;
    private BookRecyclerAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    ///new filter button added
    private Button filter;
    //list for filtering
    private ArrayList<Book> orderedList;


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
            startActivityForResult(intent, 1);
        });

        BookList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = database.getReference().child("users").child(mAuth.getUid()).child("ownedBooks");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new BookRecyclerAdapter(getActivity(), BookList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Pop up menu for book filtering
        //code for filtering application
        orderedList = new ArrayList<>();
        filter = view.findViewById(R.id.filterbtn);
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
                                for (Book orderedBook : BookList) {
                                    if (orderedBook.getStatus() == Status.Available) {
                                        orderedList.add(orderedBook);
                                    }
                                }

                                // Bind to adapter and show results
                                recyclerView.setAdapter(updatedAdapter);
                                updatedAdapter.notifyDataSetChanged();

                                return true;

                            case R.id.item3:
                                for (Book orderedBook : BookList) {
                                    if (orderedBook.getStatus() == Status.Borrowed) {
                                        orderedList.add(orderedBook);
                                    }
                                }

                                // Bind to adapter and show results
                                recyclerView.setAdapter(updatedAdapter);
                                updatedAdapter.notifyDataSetChanged();

                                return true;

                            case R.id.item4:

                                for (Book orderedBook : BookList) {
                                    if (orderedBook.getStatus() == Status.Requested) {
                                        orderedList.add(orderedBook);
                                    }
                                }

                                // Bind to adapter and show results
                                recyclerView.setAdapter(updatedAdapter);
                                updatedAdapter.notifyDataSetChanged();

                                return true;

                            case R.id.item5:

                                for (Book orderedBook : BookList) {
                                    if (orderedBook.getStatus() == Status.Accepted) {
                                        orderedList.add(orderedBook);
                                    }
                                }
                                // Bind to adapter and show results
                                recyclerView.setAdapter(updatedAdapter);
                                updatedAdapter.notifyDataSetChanged();

                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();

                if (dataSnapshot.hasChildren()){
                    Log.d("DATABASE HAS CHILDREN", "onDataChange: ");
                } else{
                    Log.d("DATABASE NO CHILDREN", "onDataChange: ");
                    BookList.clear();
                    adapter.notifyDataSetChanged();
                }

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("TestData", child.getValue().toString());
                    String key = child.getValue().toString();
                    findBook(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Gets the user's profile picture


        return view;
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
                Book b1 = new Book(isbn, title, author);
                b1.setBookID(bookID);

                    /*
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String DownloadLink = uri.toString();
                            Log.d("ImageDownload",DownloadLink);
                            b1.setImage(DownloadLink);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ImageDownload","Failed");

                        }
                    });
                    */

                if (storageRef != null) {
                    b1.setImage(key);
                }
                BookList.add(b1);
                adapter.notifyDataSetChanged();
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

