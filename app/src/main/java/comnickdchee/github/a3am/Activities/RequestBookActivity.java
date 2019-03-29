package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
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
import java.util.HashMap;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAuthor;
    TextView tvBookTitle;
    TextView tvISBN;
    TextView tvStatus;
    Button bRequestButton;
    TextView ownerName;
    Button messageOwner;
    String DownloadLink;
    ImageView bookImage;
    CircleImageView ownerImage;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
    private Book book;
    Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_book);

        tvAuthor = findViewById(R.id.authorTv);
        tvBookTitle = findViewById(R.id.bookTitleTv);
        tvISBN = findViewById(R.id.ISBNTv);
        tvStatus = findViewById(R.id.statusTv);
        bRequestButton = findViewById(R.id.buttonRequestBook);
        bookImage = findViewById(R.id.bookImageRequestBook);
        ownerImage = findViewById(R.id.userImageSeeOwnerProfile);
        ownerName = findViewById(R.id.ownerNameRequestBook);

        // Init button to be disabled
        bRequestButton.setEnabled(false);
        bRequestButton.setText("Requested");
        Resources res = getResources();
        Drawable color = res.getDrawable(R.drawable.disabled_button);
        bRequestButton.setBackground(color);

        String userID = backend.getCurrentUser().getUserID();


        // TODO: Make this error free.
        Intent intent = getIntent();
        book = (Book) intent.getExtras().getParcelable("SearchBook");

        tvAuthor.setText(book.getAuthor());
        tvBookTitle.setText(book.getTitle());
        tvISBN.setText(book.getISBN());
        tvStatus.setText(book.getStatus().toString());
        ArrayList<String> requesterList = book.getRequests();

        // Add button back if user has not requested the book before
        if (!requesterList.contains(userID)) {
            bRequestButton.setEnabled(true);
            bRequestButton.setText("Request");
            res = getResources();
            color = res.getDrawable(R.drawable.signup_custom);
            bRequestButton.setBackground(color);
        }



        Log.d(book.getBookID(), "onCreate: BookID");

        // Set request button to have an OnClick listener that fires every
        // time the activity gets pressed
        bRequestButton.setOnClickListener(this);

        // Set image of book.
        loadImageFromBookID(bookImage, book.getBookID());
        loadImageFromOwnerID(ownerImage, book.getOwnerID());

        messageOwner = findViewById(R.id.buttonMessageOwner);
        messageOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RequestBookActivity.this, "Messaging", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (getApplicationContext(), messageActivity.class);
                String key = book.getOwnerID();
                i.putExtra("key", key);
                i.putExtra("imgUrl", DownloadLink);
                Log.d("keyIn",key);
                startActivity(i);
            }
        });

        DatabaseReference ref = mFirebaseDatabase.getReference().child("users").child(book.getOwnerID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("userName").getValue().toString().trim();
                ownerName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRequestBook:
                if (mAuth.getCurrentUser() != null) {
                    // Once the user requests the current book,
                    // we add them to the array list on the condition
                    // that the current user is not a duplicate, and
                    // we change the view of the button
//                    if (!book.getRequests().contains(mAuth.getCurrentUser().getUid())) {
//                        book.addRequest(mAuth.getCurrentUser().getUid());
//                        updateRequests(book);

                    // Disables the button since to stop multiple requests
                    bRequestButton.setEnabled(false);
                    bRequestButton.setText("Requested");
                    Resources res = getResources();
                    Drawable color = res.getDrawable(R.drawable.disabled_button);
                    bRequestButton.setBackground(color);

                    String receiverKey = book.getOwnerID();
                    String senderKey = mAuth.getUid();
                    backend.updateRequests(book);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from",mAuth.getCurrentUser().getUid());
                    notificationData.put("type","request");

                    reference.child("notificationRequests").child(receiverKey).child(mAuth.getCurrentUser().getDisplayName()).push().setValue(notificationData);



//                    }

                    // TODO: UI fix; change the request button to be non-clickable.
                }

        }
    }

    /** Overwrite the data of the original book
     with the new book in Firebase */
    private void updateRequests(Book updatedBook) {
        DatabaseReference booksRef = mDatabaseReference.child("books");
        // Change this if its status is available
        if (updatedBook.getStatus().equals(Status.Available)) {
            updatedBook.setStatus(Status.Requested);
        }

        // Update the actual contents in Firebase
        Log.d(updatedBook.getBookID(), "updateRequests: ");
        booksRef.child(updatedBook.getBookID()).setValue(updatedBook);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    public void loadImageFromBookID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(bookID);
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


    public void loadImageFromOwnerID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(book.getOwnerID()+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

    /*
    public void findUserByBookID(String BookID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users");
        Log.d("refRequest", ref.toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    if(data.child("owned_books") != null){
                        //Log.d("refRequestBook", data.getValue().toString());
                        if(data.child("owned_books").getValue() != null){
                            Log.d("refRequestBook", data.child("owned_books").getValue().toString());
                            Log.d("refReq", BookID);
                            for(DataSnapshot d: data.child("owned_books").getChildren()){
                                Log.d("refReq", d.getValue().toString().trim());
                                if(BookID.trim() == d.getValue().toString().trim()){
                                    Log.d("refReq", "TRUE");
                                }
                            }
                        }
                    }
                    //Log.d("refRequestBook", data.getValue().toString());
                    //Log.d("refRequestBook", data.child("owned_books").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */
}
