package comnickdchee.github.a3am;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import comnickdchee.github.a3am.Activities.HomepageActivity;
import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;

import comnickdchee.github.a3am.Adapters.ViewPagerAdapter;

/**
 * @auhtor
 * HomeFragment extends Fragment
 * It overwrites onCreateView
 *
 */
public class HomeFragment extends Fragment {


    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout navTabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Gets the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.pagerHomepage);

        //Gets the ViewPage adapter based the data fed
        adapter = new ViewPagerAdapter(getChildFragmentManager(), HomepageActivity.BorrowedList,HomepageActivity.LendingList,
                HomepageActivity.ActionsList,HomepageActivity.RequestsList);
        viewPager.setAdapter(adapter);

        // Sets up the ViewPager the the layout
        navTabLayout = view.findViewById(R.id.navTabs);
        navTabLayout.setupWithViewPager(viewPager);
        return view;

    }
}
