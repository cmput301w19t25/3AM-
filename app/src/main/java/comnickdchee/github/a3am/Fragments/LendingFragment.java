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

import comnickdchee.github.a3am.Adapters.LendingTabAdapter;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

public class LendingFragment extends Fragment {

    private static final String TAG = "LendingFragment";
    private ArrayList<Book> data = new ArrayList<>();

    public LendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        //Get the data for recycler view items
        Bundle args = getArguments();
        data = (ArrayList<Book>) args.getSerializable("data");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //Create and return the recyclerView
        LendingTabAdapter adapter = new LendingTabAdapter(getActivity(),data);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Finished View");
        return view;

    }

}
