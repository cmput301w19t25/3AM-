package comnickdchee.github.a3am.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;
import comnickdchee.github.a3am.R;

public class HomepageActivity extends AppCompatActivity {
    private Toolbar navToolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout navTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        navToolbar = findViewById(R.id.navToolbar);
        setSupportActionBar(navToolbar);

        // view pager
        viewPager = findViewById(R.id.pagerHomepage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // tabs
        navTabLayout = findViewById(R.id.navTabs);
        navTabLayout.setupWithViewPager(viewPager);
    }
}
