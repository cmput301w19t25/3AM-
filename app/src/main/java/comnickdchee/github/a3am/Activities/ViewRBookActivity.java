package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
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

    private static final int ISBN_READ = 42;
    private RecyclerView rvRequests;
    private static ArrayList<String> requesters;
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button receiveButton;
    private ImageView backButton;
    private TextView phoneNumberText;
    private TextView emailText;
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private Marker marker;

    private Book actionBook = new Book();
    private User owner = new User();
    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_r_books);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey_default));

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(ViewRBookActivity.this);
        }

        phoneNumberText = findViewById(R.id.tvPhoneNumber);
        emailText = findViewById(R.id.tvEmail);


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

        receiveButton = findViewById(R.id.receiveBookButton);

        backend.getExchange(actionBook, new ExchangeCallback() {
            @Override
            public void onCallback(Exchange exchange) {
                if (exchange != null) {
                    if (exchange.getType() == ExchangeType.BorrowerReceive) {

                        if (exchange.getPickupCoords() != null) {
                            receiveButton.setVisibility(View.VISIBLE);
                        } else {
                            if (mapFragment.getView() != null) {
                                mapFragment.getView().setVisibility(View.GONE);
                            }
                        }

                    } else {
                        receiveButton.setVisibility(View.GONE);
                    }
                } else {
                    receiveButton.setVisibility(View.GONE);

                    if (mapFragment.getView() != null) {
                        mapFragment.getView().setVisibility(View.GONE);
                    }
                }
            }
        });

        backButton = findViewById(R.id.backIV);

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

    public void getPageData() {
        ImageView userPhoto = findViewById(R.id.ownerIV);
        ImageView bookImage = findViewById(R.id.ivViewBookPhoto);
        //TextView phone = findViewById(R.id.phoneTV);
        //TextView email = findViewById(R.id.emailTV);
        TextView username = findViewById(R.id.usernameTV);
        TextView bookTitle = findViewById(R.id.tvViewBookTitle);
        TextView bookAuthor = findViewById(R.id.tvViewBookAuthor);
        TextView bookISBN = findViewById(R.id.tvViewBookISBN);

        Log.d(owner.getUserName(), "onCallback: Borrower");
        loadImageFromOwnerID(userPhoto,owner.getUserID());
        loadImageFromBookID(bookImage, actionBook.getBookID());
        username.setText(owner.getUserName());
        //phone.setText(owner.getPhoneNumber());
        //email.setText(owner.getEmail());
        ///TODO: rating.setText(borrower.getRating());
        Log.d(actionBook.getISBN(), "getPageData: ");
        bookTitle.setText(actionBook.getTitle());
        bookAuthor.setText(actionBook.getAuthor());
        bookISBN.setText(actionBook.getISBN());
        phoneNumberText.setText(owner.getPhoneNumber());
        emailText.setText(owner.getEmail());

    }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getPickupCoords();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

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
