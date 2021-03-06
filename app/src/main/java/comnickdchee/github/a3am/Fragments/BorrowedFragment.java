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
    private TextView noDataView;

    /** Required empty constructor since its a fragment */
    public BorrowedFragment() {
        // Required empty public constructor
    }

    /** Creates the view by getting the respective book list and calling the Borrowed tab adapter */
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
        BorrowedTabAdapter adapter = new BorrowedTabAdapter(getActivity(),borrowedBooksList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // gets the data from backend
        backend.getBorrowedBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                borrowedBooksList.clear();
                borrowedBooksList.addAll(0,books);
                if (borrowedBooksList.size() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });

        return view;

    }

}
