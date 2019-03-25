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

import comnickdchee.github.a3am.Adapters.BorrowedTabAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

public class BorrowedFragment extends Fragment {

    private static final String TAG = "BorrowedFragment";
    private ArrayList<Book> borrowedBooksList = new ArrayList<>();
    private Backend backend = Backend.getBackendInstance();

    public BorrowedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //Create and return the recyclerView
        BorrowedTabAdapter adapter = new BorrowedTabAdapter(getActivity(),borrowedBooksList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        backend.getBorrowedBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                borrowedBooksList.clear();
                borrowedBooksList.addAll(0,books);
                adapter.notifyDataSetChanged();
            }
        });

        return view;

    }

}
