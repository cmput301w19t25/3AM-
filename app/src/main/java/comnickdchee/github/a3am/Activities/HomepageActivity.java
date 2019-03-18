package comnickdchee.github.a3am.Activities;

import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Fragments.MyBooksFragment;
import comnickdchee.github.a3am.Fragments.ProfileFragment;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Asma, Ismaeel, Nicholas, Tatenda & Zaheen
 * HomePageActivity extends AppCompatActivity
 * HomePageActivity implements NavigationView.onNavigationItemSelectedListener
 * @see AppCompatActivity
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
    private ActionBarDrawerToggle toggle;
    private String DownloadLink;
    private SearchView searchView;
    public static ArrayList<Book> BorrowedList = new ArrayList<>();
    public static ArrayList<Book> LendingList = new ArrayList<>();
    public static ArrayList<Book> ActionsList = new ArrayList<>();
    public static ArrayList<Book> RequestsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();

        navToolbar = findViewById(R.id.navToolbar);
        setSupportActionBar(navToolbar);

        // Setting the side Navigation Drawer
        // source: https://www.youtube.com/watch?v=fGcMLu1GJEc
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
            Editing data to add in user information for navbar.
         */
        //Sets username to the side navigation menu
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
        // sets the states of the side navigation menu
        toggle = new ActionBarDrawerToggle(this, drawer, navToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // at the start of the program will open HomeFragment before clicking on anything else
        // Sets the home pages as the active fragment if there is no saved instances
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

        // The switch cases for click of each item in the nav bar
        // Everything except the nav_logout opens the correct fragment
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
                mAuth.signOut();
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init(){
        // Initialized some sample data to be displayed

        Book b1 = new Book("1111111111","Title1","AuthorName1");
        Book b2 = new Book("1111111112","Title2","AuthorName2");
        Book b3 = new Book("1111111113","Title3","AuthorName3");
        ActionsList.add(b1);
        ActionsList.add(b2);
        ActionsList.add(b3);

        Book placeHolder = new Book("PlaceHolder","PlaceHolder","PlaceHolder");
        BorrowedList.add(placeHolder);
        LendingList.add(placeHolder);
        RequestsList.add(placeHolder);


    }

    /**
     * So that when back button is pressed while navigation drawer is open, we don't exit the activity
     * immediately instead close the navigation menu drawer first.
     */
    @Override
    public void onBackPressed() {

        // Make the back button close the navigation menu if it is open
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Inflate the menu when the SearchView item is pressed with the
     * search menu. The toolbar gets a search view button implemented
     * on the right side.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

}

