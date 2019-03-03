package comnickdchee.github.a3am.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
import comnickdchee.github.a3am.R;

public class HomepageActivity extends AppCompatActivity {
    private Toolbar navToolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout navTabLayout;
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

        // view pager
        viewPager = findViewById(R.id.pagerHomepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),BorrowedFromList,RequesterList,
                BorrowerList,RequestedFromList);
        viewPager.setAdapter(adapter);

        // tabs
        navTabLayout = findViewById(R.id.navTabs);
        navTabLayout.setupWithViewPager(viewPager);
    }

    private void init(){
        BorrowerList.add("LMAO");
        BorrowedFromList.add("LMFAO");
        RequestedFromList.add("LOLMAO");
        RequesterList.add("????");
    }
}
