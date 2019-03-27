package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comnickdchee.github.a3am.R;

/** This activity has been autogenerated by Google. */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mGoogleMap;
    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private EditText searchEditText;

    // To check if the permission is granted
    private Boolean mLocationPermissionGranted = false;

    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Set the status bar of the current activity to red
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        searchEditText = (EditText) findViewById(R.id.etSearchText);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    // Execute our method for searching
                    String searchQuery = searchEditText.getText().toString();
                    geoLocate(searchQuery);
                }
                return false;
            }
        });

        if (googleServicesAvailable()) {
            getLocationPermission();
        }
    }

    private void geoLocate(String searchQuery) {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchQuery, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            goToLocationZoom(address.getLatitude(), address.getLongitude(), 15f);
            setMarker(address.getLocality(), address.getLatitude(), address.getLongitude());
        }

    }

    /** Gets the current location of the user's device. */
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                // Gets the last location of the device
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            goToLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 15f);

                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }

    }

    /** Called when the map is initialized */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(MapsActivity.this);
        }
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;

        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();

        // Throws a dialog if the Google Play Service is unavailable.
        } else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
    }

    /** Fires after the permissions were prompted to the user. */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; ++i) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }

                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Gets the device location if permission was granted
        if (mLocationPermissionGranted) {
            getDeviceLocation();

            // Needed to see the current device's location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            googleMap.setMyLocationEnabled(true);

            // Disable this since we have a search view blocking
            // the button anyway
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }

    public void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }

    Marker marker;

    public void geoLocate(View view) throws IOException {

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

    }

    // Sets a marker, and removes the existing one if it exists
    private void setMarker(String locality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng));

        marker = mGoogleMap.addMarker(options);
    }


}

