package comnickdchee.github.a3am.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import comnickdchee.github.a3am.Fragments.ActionsFragment;
import comnickdchee.github.a3am.Fragments.BorrowedFragment;
import comnickdchee.github.a3am.Fragments.LendingFragment;
import comnickdchee.github.a3am.Fragments.RequestsFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.RequestStatusGroup;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    
    private ArrayList<Serializable> lists = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Book> BorrowedList, ArrayList<Book> LendingList,
                            ArrayList<Book> ActionsList, ArrayList<RequestStatusGroup>RequestsList) {
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
                BorrowedFragment BF =  new BorrowedFragment();
                Bundle args = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args.putSerializable("data",this.lists.get(i));
                BF.setArguments(args);
                Log.d(TAG, "getItem: Created a Borrowed Fragment");
                return BF;
            case 1:
                LendingFragment LF =  new LendingFragment();
                Bundle args1 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args1.putSerializable("data",this.lists.get(i));
                LF.setArguments(args1);
                Log.d(TAG, "getItem: Created a Lending Fragment");
                return LF;
            case 2:
                ActionsFragment AF =  new ActionsFragment();
                Bundle args2 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args2.putSerializable("data",this.lists.get(i));
                AF.setArguments(args2);
                Log.d(TAG, "getItem: Created a Actions Fragment");
                return AF;
            default:
                RequestsFragment RF =  new RequestsFragment();
                Bundle args3 = new Bundle();
                Log.d(TAG, "getItem: " + Integer.toString(i) + ": " + lists.get(i));
                args3.putSerializable("data",this.lists.get(i));
                RF.setArguments(args3);
                Log.d(TAG, "getItem: Created a Requests Fragment");
                return RF;

        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
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
