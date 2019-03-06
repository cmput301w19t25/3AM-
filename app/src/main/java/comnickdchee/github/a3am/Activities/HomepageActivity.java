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
import android.view.MenuItem;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
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

    private ArrayList<String> BorrowerList = new ArrayList<>();
    private ArrayList<String> RequesterList = new ArrayList<>();
    private ArrayList<String> BorrowedFromList = new ArrayList<>();
    private ArrayList<String> RequestedFromList = new ArrayList<>();

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, navToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.pagerHomepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),BorrowedFromList,RequesterList,
                BorrowerList,RequestedFromList);
        viewPager.setAdapter(adapter);

        // tabs
        navTabLayout = findViewById(R.id.navTabs);
        navTabLayout.setupWithViewPager(viewPager);
        // view pager
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:

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
