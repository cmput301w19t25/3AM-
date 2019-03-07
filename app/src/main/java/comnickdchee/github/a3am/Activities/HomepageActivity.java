package comnickdchee.github.a3am.Activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
import comnickdchee.github.a3am.HomeFragment;
import comnickdchee.github.a3am.LogoutFragment;
import comnickdchee.github.a3am.MessageFragment;
import comnickdchee.github.a3am.MyBooksFragment;
import comnickdchee.github.a3am.ProfileFragment;
import comnickdchee.github.a3am.R;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar navToolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout navTabLayout;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private FirebaseDatabase Fd;
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
        View hView = navigationView.getHeaderView(0);
        TextView tv = (TextView)hView.findViewById(R.id.UsernameNavbar);
        ImageView iv = (ImageView)hView.findViewById(R.id.UserImageNavbar);
        mAuth = FirebaseAuth.getInstance();
        String userN = mAuth.getCurrentUser().getDisplayName();
        String userEmail = mAuth.getCurrentUser().getEmail();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference(userEmail+"/"+"dp"+ ".jpg");
        StorageReference pathReference = storageRef.child(userEmail+"/"+"dp"+ ".jpg");
        StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://am-d5edb.appspot.com"+"/"+userEmail+"/"+"dp"+ ".jpg");
        gsReference.child("/"+userEmail+"/"+"dp"+ ".jpg").getDownloadUrl();

        String profileImageUrl = profileImageRef.getDownloadUrl().toString();
        Log.d("ProfileImage",profileImageUrl);
        tv.setText(userN);
        Glide.with(this).load(profileImageUrl).into(iv);


        //

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, navToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // view pager
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LogoutFragment()).commit();
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
