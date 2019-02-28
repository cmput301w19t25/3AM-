package comnickdchee.github.a3am.Adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import comnickdchee.github.a3am.Fragments.BorrowedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        // TODO: Add list of fragments to switch between tabs.
        return new BorrowedFragment();
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
