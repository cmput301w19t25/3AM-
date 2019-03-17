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
import comnickdchee.github.a3am.Fragments.RequestsFragment;
import comnickdchee.github.a3am.Models.Book;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    
    private ArrayList<ArrayList<Book>> lists = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Book> BorrowedList, ArrayList<Book> LendingList,
                            ArrayList<Book> ActionsList, ArrayList<Book>RequestsList) {
        super(fm);

        this.lists.add(BorrowedList);
        this.lists.add(LendingList);
        this.lists.add(ActionsList);
        this.lists.add(RequestsList);
        Log.d(TAG, "Added the lists in List");
    }

    @Override
    public Fragment getItem(int i) {
        // TODO: Add list of fragments to switch between tabs.
        switch(i) {
            case 0:
                BorrowedFragment BL =  new BorrowedFragment();
                Bundle args = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args.putSerializable("data",this.lists.get(i));
                BL.setArguments(args);
                Log.d(TAG, "getItem: Created a Borrowed Fragment");
                return BL;
            case 1:
                BorrowedFragment BL1 =  new BorrowedFragment();
                Bundle args1 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args1.putSerializable("data",this.lists.get(i));
                BL1.setArguments(args1);
                Log.d(TAG, "getItem: Created a Borrowed Fragment");
                return BL1;
            case 2:
                BorrowedFragment BL2 =  new BorrowedFragment();
                Bundle args2 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args2.putSerializable("data",this.lists.get(i));
                BL2.setArguments(args2);
                Log.d(TAG, "getItem: Created a Borrowed Fragment");
                return BL2;
            default:
                RequestsFragment RF =  new RequestsFragment();
                Bundle args3 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args3.putSerializable("data",this.lists.get(i));
                RF.setArguments(args3);
                Log.d(TAG, "getItem: Created a Borrowed Fragment");
                return RF;

        }



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
