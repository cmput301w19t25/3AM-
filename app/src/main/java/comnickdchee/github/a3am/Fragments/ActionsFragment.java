package comnickdchee.github.a3am.Fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

import comnickdchee.github.a3am.Adapters.ActionsTabAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

/**
 * A simple {@link Fragment} subclass.
 */

// BLA stands for Borrowed, Lending, Actions because they all share the same type of data

public class ActionsFragment extends Fragment {

    private static final String TAG = "ActionsFragment";
    private ArrayList<Book> data = new ArrayList<>();
    private FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mFireBaseDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private ArrayList<Book> actionsBooksList = new ArrayList<>();
    private TextView noDataView;
    Backend backend = Backend.getBackendInstance();

    /** Required empty constructor since its a fragment */
    public ActionsFragment() {
        // Required empty public constructor
    }

    /** Creates the view by getting the respective book list and calling the actions tab adapter */
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
        ActionsTabAdapter adapter = new ActionsTabAdapter(getActivity(), actionsBooksList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        backend.getActionsBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                actionsBooksList.clear();
                actionsBooksList.addAll(0,books);
                if (actionsBooksList.size() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noDataView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataView.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });

        Log.d(TAG, "onCreateView: Finished View");
        return view;

    }

}
