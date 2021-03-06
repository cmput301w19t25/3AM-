package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.lang.reflect.Method;
import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RequestersAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.ExchangeCallback;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.PickupCoords;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * View Book Activity - Activity that lets owners look at the
 * requests on a given book that they own. The user can accept
 * and decline requests.
 */
public class ViewRBookActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Intent request code for ISBN scanner
    private static final int ISBN_READ = 42;

    // View objects
    private Button receiveButton;
    private TextView phoneNumberText;
    private TextView emailText;
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private Marker marker;

    // Model objects
    private Book actionBook = new Book();
    private User owner = new User();
    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_r_books);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkRed));

        // Setting up the map fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(ViewRBookActivity.this);
        }

        // Setting up the view items
        phoneNumberText = findViewById(R.id.tvPhoneNumber);
        emailText = findViewById(R.id.tvEmail);
        ConstraintLayout userCard = findViewById(R.id.userCard);
        receiveButton = findViewById(R.id.receiveBookButton);
        ImageView backButton = findViewById(R.id.backIV);


        // Get the book data and get the owner that is associated with the user
        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("acceptedBook");
        String ownerID = actionBook.getOwnerID();
        backend.getUser(ownerID, new UserCallback() {
            @Override
            public void onCallback(User user) {
                owner = user;
                getPageData();
            }
        });


        // Shows profile when userCard is clicked
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), UserProfileActivity.class);
                i.putExtra("key", ownerID);
                startActivity(i);
            }
        });

        // Gets the exchange object from the database, where
        // we set certain views to be visible if certain conditions
        // are met
        backend.getExchange(actionBook, new ExchangeCallback() {
            @Override
            public void onCallback(Exchange exchange) {
                if (exchange != null) {
                    // If the borrower is expected to receive the book, we set it to visible
                    if (exchange.getType() == ExchangeType.BorrowerReceive) {
                        receiveButton.setVisibility(View.VISIBLE);

                        // If coordinates aren't specified, we show them in the map view
                        if (exchange.getPickupCoords() == null) {
                            if (mapFragment.getView() != null) {
                                mapFragment.getView().setVisibility(View.GONE);
                            }
                        }
                    // Otherwise, we don't let the user press the receive button
                    } else {
                        receiveButton.setVisibility(View.GONE);
                    }

                // Set everything to GONE
                } else {
                    receiveButton.setVisibility(View.GONE);
                    if (mapFragment.getView() != null) {
                        mapFragment.getView().setVisibility(View.GONE);
                    }
                }
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

            if (isbn.equals(bookISBN)){
                actionBook.setStatus(Status.Borrowed);
                actionBook.setCurrentBorrowerID(FirebaseAuth.getInstance().getUid());
                backend.updateExchange(actionBook, ExchangeType.BorrowerHandover);
                backend.updateBookData(actionBook);
                finish();
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }


        }
    }

    // Setting up most of the views in the page that don't need listeners
    public void getPageData() {
        CircleImageView userPhoto = findViewById(R.id.ownerIV);
        ImageView bookImage = findViewById(R.id.ivViewBookPhoto);
        TextView username = findViewById(R.id.usernameTV);
        TextView bookTitle = findViewById(R.id.tvViewBookTitle);
        TextView bookAuthor = findViewById(R.id.tvViewBookAuthor);
        TextView bookISBN = findViewById(R.id.tvViewBookISBN);

        loadImageFromOwnerID(userPhoto,owner.getUserID());
        loadImageFromBookID(bookImage, actionBook.getBookID());
        username.setText(owner.getUserName());
        bookTitle.setText(actionBook.getTitle());
        bookAuthor.setText(actionBook.getAuthor());
        bookISBN.setText(actionBook.getISBN());
        phoneNumberText.setText(owner.getPhoneNumber());
        emailText.setText(owner.getEmail());
    }

    /** Method that populates an Image View using the user's id as a reference. */
    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imgUrl = uri.toString();
                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });

    }

    /** Method that populates an Image View using the book's id as a reference. */
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

    /** Used for showing the map of the owner's specified location. */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getPickupCoords();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    /**
     * Method that uses Firebase listeners to get the pickup location.
     * When a map location has been added to the exchange, we get the exchange pickup
     * coordinates and set them on the map fragment in real-time.
     */
    public void getPickupCoords() {
        if (actionBook != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference exchangeRef = firebaseDatabase.getReference("exchanges").child(actionBook.getBookID());
            exchangeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Exchange exchange = dataSnapshot.getValue(Exchange.class);

                    if (exchange != null) {
                        PickupCoords pickupCoords = exchange.getPickupCoords();
                        if (pickupCoords != null) {

                            // Set the visibility here if we have a location
                            if (mapFragment.getView() != null) {
                                mapFragment.getView().setVisibility(View.VISIBLE);
                            }

                            LatLng latLng = new LatLng(pickupCoords.getLatitude(), pickupCoords.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng);
                            marker = mGoogleMap.addMarker(markerOptions);
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                        // Setting the map fragment to be invisible if no location added
                        } else {
                            if (mapFragment.getView() != null) {
                                mapFragment.getView().setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
