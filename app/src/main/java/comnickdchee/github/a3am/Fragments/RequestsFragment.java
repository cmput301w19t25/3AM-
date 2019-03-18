package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ProductAdapter;
import comnickdchee.github.a3am.Models.RequestStatusGroup;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private ArrayList<Book> data = new ArrayList<>();

    public RequestsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        Bundle args = getArguments();
        ArrayList<RequestStatusGroup> Requests = (ArrayList<RequestStatusGroup>) args.getSerializable("data");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProductAdapter adapter = new ProductAdapter(Requests);
        recyclerView.setAdapter(adapter);

        return view;

    }

}
