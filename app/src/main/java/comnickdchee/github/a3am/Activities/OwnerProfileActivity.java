package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.net.Uri;
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

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.ExchangeCallback;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
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
 * @see AppCompatActivity
 */
public class OwnerProfileActivity extends AppCompatActivity {

    ImageView backButton;
    private static final int ISBN_READ = 42;
    private Book actionBook = new Book();
    private User owner = new User();
    private Button messageButton;
    Button transactionButton;
    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkRed));

        backButton = findViewById(R.id.backIV);
        transactionButton = findViewById(R.id.transactionButton);
        transactionButton.setText("Return Book");
        messageButton = findViewById(R.id.message);

        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("passedBook");
        String ownerID = actionBook.getOwnerID();

        backend.getUser(ownerID, new UserCallback() {
            /**
             * This method is used initialize owner
             * @param user takes in an object of type User
             * @see User
             */
            @Override
            public void onCallback(User user) {
                owner = user;
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
                i.putExtra("key", ownerID);
                startActivity(i);
            }
        });

        backend.getExchange(actionBook, new ExchangeCallback() {
            /**
             * This method checks if there's any exchanges and sets the visibility of the transactionButton accordingly.
             * @param exchange
             */
            @Override
            public void onCallback(Exchange exchange) {
                if (exchange != null) {
                    if (exchange.getType() == ExchangeType.BorrowerHandover) {
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
                Intent intent = new Intent(OwnerProfileActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);
            String bookISBN = actionBook.getISBN();

            if (isbn.equals(bookISBN)) {
                backend.updateExchange(actionBook, ExchangeType.OwnerReceive);
                finish();
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
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

        Log.d(owner.getUserName(), "onCallback: Borrower");

        loadImageFromOwnerID(userPhoto,owner.getUserID());
        loadImageFromBookID(bookImage,actionBook.getBookID());
        username.setText(owner.getUserName());
        phone.setText(owner.getPhoneNumber());
        email.setText(owner.getEmail());
        ///TODO: rating.setText(borrower.getRating());
        Log.d(actionBook.getISBN(), "getPageData: ");
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

                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
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
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });

    }
}
