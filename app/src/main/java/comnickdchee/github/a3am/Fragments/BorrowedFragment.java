package comnickdchee.github.a3am.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comnickdchee.github.a3am.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowedFragment extends Fragment {


    public BorrowedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_borrowed, container, false);
    }

}
