package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import comnickdchee.github.a3am.Adapters.RequestersAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookCallback;
import comnickdchee.github.a3am.Backend.ExchangeCallback;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Backend.UserListCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.PickupCoords;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * View Book Activity - Activity that lets owners look at the
 * requests on a given book that they own. The user can accept
 * and decline requests.
 */
public class ViewBookActivity extends AppCompatActivity {

    private static final int ISBN_READ = 42;
    private static final int LOCATION_CODE = 7;
    private RecyclerView rvRequests;
    private TextView emptyView;
    private TextView locationText;
    private CardView borrowerCardView;
    private TextView borrowerUsernameText;
    private TextView borrowerPhoneNumberText;
    private CircleImageView borrowerPhoto;
    private TextView borrowerEmailText;
    private ArrayList<User> requesters = new ArrayList<>();
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ownerHandoverButton;
    private Button editLocationButton;
    private Backend backend = Backend.getBackendInstance();
    private Exchange currentExchange;
    private Book actionBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        locationText = (TextView) findViewById(R.id.tvLocation);
        emptyView = (TextView) findViewById(R.id.tvEmptyRV);
        borrowerPhoto = (CircleImageView) findViewById(R.id.ivAcceptedPhoto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //button click for owner to specify pick up location
        editLocationButton = (Button) findViewById(R.id.bChangeLocation);
        ownerHandoverButton = findViewById(R.id.bOwnerHandover);
        editLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, MapsActivity.class);
                startActivityForResult(intent, LOCATION_CODE);
            }
        });


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkRed));

        // Get the contents of the intent
        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("ActionBook");

        // Attains a reference to the current activities views
        getPageData();

        // Populate the requester cards recycler view
        rvRequests = findViewById(R.id.rvViewBookRequests);
        layoutManager = new LinearLayoutManager(this);
        requestersAdapter = new RequestersAdapter(this, requesters, actionBook);
        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestersAdapter);

        // Gets the current exchange of the given book, and sets
        // the appropriate views to be visible when certain
        // conditions are triggered
        backend.getExchange(actionBook, new ExchangeCallback() {
            @Override
            public void onCallback(Exchange exchange) {
                try {
                    if (exchange != null) {
                        currentExchange = exchange;

                        // Get the status, so we can choose to display the button
                        if (currentExchange.getType() == ExchangeType.OwnerHandover) {
                            ownerHandoverButton.setVisibility(View.VISIBLE);
                        } else {
                            ownerHandoverButton.setVisibility(View.GONE);
                        }

                        // Get the exchange's pickup coordinates and add them as a string
                        // to display in the view
                        PickupCoords coords = currentExchange.getPickupCoords();
                        if (coords != null) {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(ViewBookActivity.this, Locale.getDefault());

                            addresses = geocoder.getFromLocation(coords.getLatitude(),
                                    coords.getLongitude(), 1);

                            Address address = addresses.get(0);
                            ArrayList<String> addressFragments = new ArrayList<String>();
                            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                addressFragments.add(address.getAddressLine(i));
                            }

                            String locationString = TextUtils.join(", ", addressFragments);
                            locationText.setText(locationString);
                        }
                    } else {
                        ownerHandoverButton.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                }
            }
        });

        // Gets the book related to the current activity, and populates
        // the views accordingly
        backend.getBook(actionBook.getBookID(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                if (book != null) {
                    if (book.getCurrentBorrowerID() != null) {
                        rvRequests.setVisibility(View.GONE);
                        borrowerCardView.setVisibility(View.VISIBLE);

                        // Gets the current borrower and populates the card view
                        backend.getUser(book.getCurrentBorrowerID(), new UserCallback() {
                            @Override
                            public void onCallback(User borrower) {
                                borrowerUsernameText.setText(borrower.getUserName());
                                borrowerEmailText.setText(borrower.getEmail());
                                borrowerPhoneNumberText.setText(borrower.getPhoneNumber());
                                loadImageFromOwnerID(borrowerPhoto, borrower.getUserID());
                            }
                        });
                    } else {
                        borrowerCardView.setVisibility(View.GONE);
                    }
                }
            }
        });

        // Gets the current requests of the given book and populates
        // the recycler view as a result
        backend.getRequesters(actionBook, new UserListCallback() {
            @Override
            public void onCallback(ArrayList<User> users) {
                requesters.clear();
                requesters.addAll(users);
                requestersAdapter.notifyDataSetChanged();

                if (requesters.isEmpty()) {
                    if (actionBook.getCurrentBorrowerID() == null) {
                        emptyView.setVisibility(View.VISIBLE);
                        rvRequests.setVisibility(View.GONE);

                    } else {
                        emptyView.setVisibility(View.GONE);
                        rvRequests.setVisibility(View.GONE);

                    }
                } else {
                    rvRequests.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        // When pressed, the owner can scan the ISBN code from the book
        // to confirm that the book has be handed over
        ownerHandoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

    }

    /**
     * Method that is fired after we return from either the map activity
     * or the ISBN scanner, where we handle the appropriate logic.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            String bookISBN = actionBook.getISBN();

            // Checks if the ISBN matched
            if (isbn.equals(bookISBN)) {
                currentExchange.setType(ExchangeType.BorrowerReceive);
                backend.updateExchange(actionBook, currentExchange);
                finish();
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }

        // Returning from the maps activity
        } else if (requestCode == LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            LatLng coords = (LatLng) data.getExtras().getParcelable("Location");
            if (currentExchange == null) {
                currentExchange = new Exchange();
            }
            
            currentExchange.setPickupCoords(new PickupCoords(coords.latitude, coords.longitude));
            backend.updateExchange(actionBook, currentExchange);

            // Gets the pickup coordinates set in the map and converts it to a
            // string to display on the activity
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                addresses = geocoder.getFromLocation(coords.latitude, coords.longitude, 1);

                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }

                String locationString = TextUtils.join(", ", addressFragments);
                locationText.setText(locationString);


            } catch (IOException e) {
            }

        }
    }

    /**
     * Popualates the given references with their appropriate data
     * from the book.
     */
    public void getPageData() {
        ImageView bookImage = findViewById(R.id.ivViewBookPhoto);
        TextView bookTitle = findViewById(R.id.tvViewBookTitle);
        TextView bookAuthor = findViewById(R.id.tvViewBookAuthor);
        TextView bookISBN = findViewById(R.id.tvViewBookISBN);
        borrowerCardView = findViewById(R.id.cvEmbeddedUser);
        borrowerUsernameText = findViewById(R.id.embeddedName);
        borrowerEmailText = findViewById(R.id.embeddedCardEmail);
        borrowerPhoneNumberText = findViewById(R.id.embeddedCardPhoneNumber);

        loadImageFromBookID(bookImage, actionBook.getBookID());
        bookTitle.setText(actionBook.getTitle());
        bookAuthor.setText(actionBook.getAuthor());
        bookISBN.setText(actionBook.getISBN());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Method that populates an Image View using the book's id as a reference. */
    public void loadImageFromBookID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(bookID);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

    /** This loads the profile picture of user with userID of uID and places the image in the
     * load imageView */
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
}
