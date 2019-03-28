package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import comnickdchee.github.a3am.Backend.ExchangeCallback;
import comnickdchee.github.a3am.Backend.UserListCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.PickupCoords;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

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
    private ArrayList<User> requesters = new ArrayList<>();
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ownerHandoverButton;
    private Button editLocationButton;
    private Backend backend = Backend.getBackendInstance();
    private Book actionBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        locationText = (TextView) findViewById(R.id.tvLocation);
        emptyView = (TextView) findViewById(R.id.tvEmptyRV);

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
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey_default));

        // Get the contents of the intent
        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("ActionBook");

        getPageData();

        rvRequests = findViewById(R.id.rvViewBookRequests);

        layoutManager = new LinearLayoutManager(this);
        requestersAdapter = new RequestersAdapter(this, requesters, actionBook);
        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestersAdapter);

        backend.getExchange(actionBook, new ExchangeCallback() {
            @Override
            public void onCallback(Exchange exchange) {
                try {
                    if (exchange != null) {

                        // Get the status, so we can choose to display the button
                        if (exchange.getType() == ExchangeType.OwnerHandover) {
                            ownerHandoverButton.setVisibility(View.VISIBLE);
                        } else {
                            ownerHandoverButton.setVisibility(View.GONE);
                        }


                        PickupCoords coords = exchange.getPickupCoords();
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
                    }
                } catch (IOException e) {
                }
            }
        });

        backend.getRequesters(actionBook, new UserListCallback() {
            @Override
            public void onCallback(ArrayList<User> users) {
                requesters.clear();
                requesters.addAll(users);
                requestersAdapter.notifyDataSetChanged();
            }
        });

        // TODO: Get this to show the actual borrower
        if (requesters.isEmpty() && actionBook.getCurrentBorrowerID() == null) {
            emptyView.setVisibility(View.VISIBLE);
            rvRequests.setVisibility(View.GONE);
        } else {
            rvRequests.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        ownerHandoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set exchange type
                Intent intent = new Intent(ViewBookActivity.this, BarcodeScanner.class);
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
                backend.updateExchange(actionBook, ExchangeType.BorrowerReceive);
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == LOCATION_CODE && resultCode == RESULT_OK && data != null) {
            LatLng coords = (LatLng) data.getExtras().getParcelable("Location");
            Exchange exchange = new Exchange(new PickupCoords(coords.latitude, coords.longitude), ExchangeType.BorrowerReceive);
            backend.updateExchange(actionBook, exchange);

            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                addresses = geocoder.getFromLocation(coords.latitude, coords.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.

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

    public void getPageData() {
        ImageView bookImage = findViewById(R.id.ivViewBookPhoto);
        TextView bookTitle = findViewById(R.id.tvViewBookTitle);
        TextView bookAuthor = findViewById(R.id.tvViewBookAuthor);
        TextView bookISBN = findViewById(R.id.tvViewBookISBN);

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
