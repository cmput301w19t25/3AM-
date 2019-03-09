package comnickdchee.github.a3am.Activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
import comnickdchee.github.a3am.HomeFragment;
import comnickdchee.github.a3am.LogoutFragment;
import comnickdchee.github.a3am.MessageFragment;
import comnickdchee.github.a3am.MyBooksFragment;
import comnickdchee.github.a3am.ProfileFragment;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Asma, Ismaeel, Nicholas, Tatenda & Zaheen
 * HomePageActivity extends AppCompatActivity
 * HomePageActivity implements NavigationView.onNavigationItemSelectedListener
 */

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar navToolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout navTabLayout;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private FirebaseDatabase Fd;
    private DatabaseReference mDataRef;
    private String DownloadLink;
    public static ArrayList<String> BorrowerList = new ArrayList<>();
    public static ArrayList<String> RequesterList = new ArrayList<>();
    public static ArrayList<String> BorrowedFromList = new ArrayList<>();
    public static ArrayList<String> RequestedFromList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        navToolbar = findViewById(R.id.navToolbar);
        setSupportActionBar(navToolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
            Editing data to add in user information for navbar.
         */
        final View hView = navigationView.getHeaderView(0);
        TextView tv = (TextView)hView.findViewById(R.id.UsernameNavbar);
        mAuth = FirebaseAuth.getInstance();
        String userN = mAuth.getCurrentUser().getDisplayName();
        String userEmail = mAuth.getCurrentUser().getEmail();
        tv.setText(userN);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child(userEmail+"/"+"dp"+ ".jpg");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                CircleImageView iv = (CircleImageView) hView.findViewById(R.id.UserImageNavbar);
                Picasso.with(getApplicationContext()).load(uri.toString()).fit().into(iv);
                //Handle whatever you're going to do with the URL here
            }
        });
        //Picasso.with(this).load(DownloadLink).fit().centerCrop().into(iv);
        //StorageReference profileImageRef = FirebaseStorage.getInstance().getReference(userEmail+"/"+"dp"+ ".jpg");

        //StorageReference gsReference = storage.getReferenceFromUrl();
        //Log.d("Image to Download",profileImageRef.toString());

        /*
        String x = gsReference.child("/"+userEmail+"/"+"dp"+ ".jpg").getDownloadUrl().toString();

        String profileImageUrl = profileImageRef.getDownloadUrl().toString();
        Log.d("ProfileImage",x);
        */


        //

        /**
         * create an instance of ActionBarDrawerToggle
         * it creates menu button and handles click events
         * @param this context which is this activity
         * @param drawer
         * @param navToolbar
         * @param R.string.navigation_drawer_open
         * @param R.string.navigation_drawer_open
         *
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, navToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();   //rotating the hambugur item


        // view pager
        //AT the start of the program will open HomeFragment before clicking on anything else
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }

    /**
     *
     * @param menuItem selected Item
     * @return true if an item was selected
     * switch statements which check for the different menu items by id
     * opens a specific fragment depending on which item was selected from the menu
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_books:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyBooksFragment()).commit();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;

            case R.id.nav_logout:
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init(){
        BorrowerList.add("LMAO");
        BorrowedFromList.add("LMFAO");
        RequestedFromList.add("LOLMAO");
        RequesterList.add("????");
    }

    /**
     * Overwrite onBackPressed
     * So that when back button is pressed while navigation drawer is open, we don't exit the activity
     * immediately instead close the navigation menu drawer first
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}

