package comnickdchee.github.a3am.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.ExchangeCallback;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author cmput301w19t25
 * This class extends AppCompatActivity
 */
public class BorrowedProfileActivity extends AppCompatActivity {

    ImageView backButton;
    Button transactionButton;
    private static final int ISBN_READ = 42;
    private Book actionBook = new Book();
    private User borrower = new User();
    private Context mContext;
    private Button messageButton;
    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        setContentView(R.layout.activity_user_profile);

        backButton = findViewById(R.id.backIV);
        transactionButton = findViewById(R.id.transactionButton);
        transactionButton.setText("Confirm Return");
        messageButton = findViewById(R.id.message);

        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("passedBook");
        String borrowerID = actionBook.getCurrentBorrowerID();
        backend.getUser(borrowerID, new UserCallback() {
            /**
             * This method is used initialize borrower
             * @param user takes in an object of type User
             * @see User
             */
            @Override
            public void onCallback(User user) {
                borrower = user;
                getPageData();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is used to set an onClick listener on message button that opens the messaging
             * screen when clicked.
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), messageActivity.class);
                i.putExtra("key", borrowerID);
                startActivity(i);
            }
        });

        backend.getExchange(actionBook, new ExchangeCallback() {
            /**
             *
             * @param exchange
             */
            @Override
            public void onCallback(Exchange exchange) {
                if (exchange != null) {
                    if (exchange.getType() == ExchangeType.OwnerReceive) {
                        transactionButton.setVisibility(View.VISIBLE);
                    } else {
                        transactionButton.setVisibility(View.GONE);
                    }
                } else {
                    transactionButton.setVisibility(View.GONE);
                }
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is used to set an onClick listener on the back arrow button
             * @param view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        transactionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is used to set an onClick listener on the scan button that launches the barcode Scanner when clicked
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowedProfileActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

    }

    /** Result to check if the ISBN scanned was successful. */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);

            String bookISBN = actionBook.getISBN();

            // Signifies that the transaction is complete. So, we update
            // the data in Firebase as if no transaction ever happened
            if (isbn.equals(bookISBN)) {
                actionBook.setStatus(Status.Available);
                actionBook.setCurrentBorrowerID(null);
                backend.updateBookData(actionBook);
                backend.deleteExchange(actionBook);

                // Added: Borrower's requested books is updated with
                // the current book removed from his list
                borrower.getRequestedBooks().remove(actionBook.getBookID());
                backend.updateUserData(borrower);

                // Finish the activity
                finish();

            } else {
                Toast.makeText(this, "ISBN not matched with book", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method is used to get the data of the book's owner's profile. i.e; phone, email, etc of the book owner
     */
    public void getPageData() {
        CircleImageView userPhoto = findViewById(R.id.UserImage);
        ImageView bookImage = findViewById(R.id.bookImage);
        TextView phone = findViewById(R.id.phoneTV);
        TextView email = findViewById(R.id.emailTV);
        TextView username = findViewById(R.id.usernameTV);
        TextView bookTitle = findViewById(R.id.bookTitleTV);
        TextView bookAuthor = findViewById(R.id.authorNameTV);
        TextView bookISBN = findViewById(R.id.ISBNTv);

        loadImageFromOwnerID(userPhoto,borrower.getUserID());
        loadImageFromBookID(bookImage, actionBook.getBookID());
        username.setText(borrower.getUserName());
        phone.setText(borrower.getPhoneNumber());
        email.setText(borrower.getEmail());
        bookTitle.setText(actionBook.getTitle());
        bookAuthor.setText(actionBook.getAuthor());
        bookISBN.setText(actionBook.getISBN());

    }

    /**
     * This method is used to the profile picture of the book owner.
     * @param load
     * @param uID
     */
    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

    /**
     *
     * This method is used to load the image of the book.
     * @param load
     * @param bookID
     */
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

}
