package comnickdchee.github.a3am.Adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import comnickdchee.github.a3am.Fragments.BorrowedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    
    private ArrayList<ArrayList<String>> lists = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> BorrowedFromList, ArrayList<String> RequesterList,
                            ArrayList<String> BorrowerList, ArrayList<String>RequestedFromList) {
        super(fm);

        this.lists.add(BorrowerList);
        this.lists.add(BorrowedFromList);
        this.lists.add(RequestedFromList);
        this.lists.add(RequesterList);
        Log.d(TAG, "Added the lists in List");
    }

    @Override
    public Fragment getItem(int i) {
        // TODO: Add list of fragments to switch between tabs.

        BorrowedFragment BL =  new BorrowedFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
        args.putStringArrayList("data",this.lists.get(i));
        BL.setArguments(args);
        Log.d(TAG, "getItem: Created a Borrowed Fragment");
        return BL;
    }

    /** Returns number of tabs. */
    @Override
    public int getCount() {
        return 4;
    }

    /** Returns page tab titles */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Borrowed";
            case 1:
                return "Lending";
            case 2:
                return "Actions";
            case 3:
                return "Requests";
            default:
                // TODO: Throw exception if we got here.
                return "None";
        }
    }
}
