package comnickdchee.github.a3am.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comnickdchee.github.a3am.Activities.HomepageActivity;
import comnickdchee.github.a3am.Adapters.RecyclerViewAdapter;
import comnickdchee.github.a3am.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowedFragment extends Fragment {

    private static final String TAG = "BorrowedFragment";
    private ArrayList<String> data = new ArrayList<>();

    public BorrowedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Started OnCreate");
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        Bundle args = getArguments();
        ArrayList<String> data = args.getStringArrayList("data");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        Log.d(TAG, "onCreateView: Received Data: " );
        
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),data);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Finished View");
        return view;

    }



}
