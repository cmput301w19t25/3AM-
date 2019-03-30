package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.LendingTabAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

public class LendingFragment extends Fragment {

    private static final String TAG = "LendingFragment";
    private ArrayList<Book> lendingBooksList = new ArrayList<>();
    private Backend backend = Backend.getBackendInstance();
    private TextView noDataView;

    public LendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noDataView = view.findViewById(R.id.noDataView);

        recyclerView.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.INVISIBLE);

        //Create and return the recyclerView
        LendingTabAdapter adapter = new LendingTabAdapter(getActivity(), lendingBooksList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        backend.getLendingBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                lendingBooksList.clear();
                lendingBooksList.addAll(0,books);
                if (lendingBooksList.size() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.INVISIBLE);
                }
                Log.d(TAG, "onCallback: " + Integer.toString(books.size()));
                adapter.notifyDataSetChanged();
            }
        });

        return view;

    }


}
