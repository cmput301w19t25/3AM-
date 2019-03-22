package comnickdchee.github.a3am.Fragments;

import android.content.Intent;
import android.net.Uri;
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

import comnickdchee.github.a3am.Activities.NewBookActivity;
import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Models.Book;
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

        DatabaseReference ref = database.getReference().child("users").child(mAuth.getUid()).child("owned_books");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new BookRecyclerAdapter(getActivity(), BookList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getKey();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Log.d("TestData",child.getValue().toString());
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
    public void findBook(final String key){
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
                        FirebaseStorage.getInstance().getReference("shelf@gmail.com"+"/"+"dp"+ ".jpg");
                Log.d("TestImageBook", profileImageRef.toString());

                Log.d("TestDataBook",dataSnapshot.child(key).getValue().toString());
                    String author = dataSnapshot.child(key).child("author").getValue().toString();
                    String isbn = dataSnapshot.child(key).child("isbn").getValue().toString();
                    String title = dataSnapshot.child(key).child("title").getValue().toString();
                    Book b1 = new Book(isbn,title,author);

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
        Log.d("TestDataBook",s);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        adapter.notifyDataSetChanged();
    }

}
