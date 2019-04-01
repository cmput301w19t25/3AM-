package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.content.res.Resources;
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
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author cmput301w19t25
 * This class extends AppCompatActivity
 * and implements View.OnClickListener
 * @see AppCompatActivity
 * @see View.OnClickListener
 * Activity that handles requesting books from the user's page
 * when they click on a book from the search results activity.
 * This activity enables users to request the owner's book,
 * view the owner's profile, and message the owner.
 */
public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    // View/UI objects
    TextView tvAuthor;
    TextView tvBookTitle;
    TextView tvISBN;
    TextView tvStatus;
    Button bRequestButton;
    TextView ownerName;
    Button messageOwner;
    ImageView backButton;
    String DownloadLink;
    ImageView bookImage;
    CircleImageView ownerImage;

    // Firebase references
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // Model objects
    private Book book;

    // Backend reference
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
        backButton = findViewById(R.id.backIV2);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Retrieve the book data passed from the intent
        Intent intent = getIntent();
        book = (Book) intent.getExtras().getParcelable("SearchBook");

        // Populate the views
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

        // Handles the logic for messaging the owner
        messageOwner = findViewById(R.id.buttonViewProfile);
        messageOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), UserProfileActivity.class);
                String key = book.getOwnerID();
                i.putExtra("key", key);
                Log.d("keyIn",key);
                startActivity(i);
            }
        });

        // Create a listener that gets the owner's information and populate the given views
        DatabaseReference ref = mFirebaseDatabase.getReference().child("users").child(book.getOwnerID());
        ref.addValueEventListener(new ValueEventListener() {
            /**
             * Overrides onDataChange
             * @param dataSnapshot
             * gets username from dataSnapshot
             * uses username to set owner name
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("userName").getValue().toString().trim();
                ownerName.setText(userName);
            }

            /**
             * This method override onCancelled(DatabaseError databaseError)
             * @param databaseError
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is used to disable the request button once a borrower places a request on a certain book.
     * This is to avoid having having one user placing more than one request on one book.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRequestBook:
                if (mAuth.getCurrentUser() != null) {
                    // Once the user requests the current book,
                    // we add them to the array list on the condition
                    // that the current user is not a duplicate, and
                    // we change the view of the button

                    // Disables the button since to stop multiple requests
                    bRequestButton.setEnabled(false);
                    bRequestButton.setText("Requested");
                    Resources res = getResources();
                    Drawable color = res.getDrawable(R.drawable.disabled_button);
                    bRequestButton.setBackground(color);

                    // Getting the respective uids to coordinate a push notification
                    String receiverKey = book.getOwnerID();
                    String senderKey = mAuth.getUid();

                    // Here, we update the actual request in firebase
                    backend.updateRequests(book);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    // Creating entries in the Firebase table that stores the notification data
                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from",mAuth.getCurrentUser().getUid());
                    notificationData.put("type","request");

                    reference.child("notificationRequests")
                            .child(receiverKey)
                            .child(mAuth.getCurrentUser().getDisplayName())
                            .push()
                            .setValue(notificationData);
                }

        }
    }

    /**
     * overrides onPinterCaptureChanged
     * @param hasCapture
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }


    /** Method that populates an Image View using the book's id as a reference.
     * This method is used to load the picture of the book.
     * @param load
     * @param bookID
     */
    public void loadImageFromBookID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(bookID);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });

    }



    /** Method that populates an Image View using the book's owner id as a reference.
     * This method is used to load the profile picture of the book owner.
     * @param load
     * @param bookID
     */
    public void loadImageFromOwnerID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(book.getOwnerID()+".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });
    }
}
