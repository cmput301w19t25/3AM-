package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import comnickdchee.github.a3am.Adapters.PlaceAutocompleteAdapter;
import comnickdchee.github.a3am.R;

/** The maps activity that allows users to search for locations in
 * the search bar, and drag a pin to enter a location. By default,
 * the current location is added */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mGoogleMap;

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private AutoCompleteTextView searchEditText;
    private Button setLocationButton;

    // To check if the permission is granted
    private Boolean mLocationPermissionGranted = false;

    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private LatLng pinnedLocation;

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

        searchEditText = (AutoCompleteTextView) findViewById(R.id.etSearchText);
        setLocationButton = (Button) findViewById(R.id.bSetLocation);

        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker != null) {
                    Intent locationData = new Intent();
                    locationData.putExtra("Location", pinnedLocation);
                    setResult(RESULT_OK, locationData);
                    finish();

                } else {
                    Toast.makeText(MapsActivity.this, "No location specified", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adapter for autocomplete
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        searchEditText.setAdapter(placeAutocompleteAdapter);

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
        hideSoftKeyboard();
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

                          if (task.getResult() != null) {

                                Location currentLocation = (Location) task.getResult();
                                goToLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 15f);

                              //add a marker to the current location
                              Geocoder geocoder = new Geocoder(MapsActivity.this);
                              Address address2 = null;
                              List<Address> list2 = new ArrayList<>();
                              try {
                                  list2 = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                  address2 = list2.get(0);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                              setMarker(address2.getLocality(), currentLocation.getLatitude(), currentLocation.getLongitude());
                            }
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

    /**
     * Gets the current location permission from the current user. This is used
     * for when we want to start the location of the map view.
     */
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

    /**
     * Called when the map is ready. We check if location is permitted,
     * otherwise we prompt for permission. Then we get the device's location
     * and set the current location to be the user's current location.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if(mGoogleMap != null) {

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Geocoder gc = new Geocoder(MapsActivity.this);
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address addr = list.get(0);
                    marker.setTitle(addr.getLocality());
                    setMarker(addr.getLocality(), addr.getLatitude(), addr.getLongitude());
                }
            });

            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    Geocoder gc = new Geocoder(MapsActivity.this);
                    LatLng ll = marker.getPosition();
                    List<Address> list = null;
                    try {
                       list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address addr = list.get(0);
                    marker.setTitle(addr.getLocality());
                    setMarker(addr.getLocality(), addr.getLatitude(), addr.getLongitude());


                }
            });
        }

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

    /**
     * Zooms in on the location, used when the user
     * has entered a location in the search bar and is expecting a result
     * with a pin showing in the map.
     */
    public void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(update);
        }
        hideSoftKeyboard();
    }

    Marker marker;

    // Sets a marker, and removes the existing one if it exists
    private void setMarker(String locality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }

        pinnedLocation = new LatLng(lat, lng);

        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .title(locality)
                .position(pinnedLocation);

        if (mGoogleMap != null) {
            marker = mGoogleMap.addMarker(options);
        }
    }

    /** Used to hide the keyboard when the address is entered. */
    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}

