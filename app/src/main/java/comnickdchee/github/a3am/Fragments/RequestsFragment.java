package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RequestsTabAdapter;
import comnickdchee.github.a3am.Models.RequestStatusGroup;
import comnickdchee.github.a3am.R;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private ArrayList<RequestStatusGroup> Requests = new ArrayList<>();

    public RequestsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);


        // Gets arguments to feed into adapter for recyclerView
        Bundle args = getArguments();
        Requests = (ArrayList<RequestStatusGroup>) args.getSerializable("data");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Passes argument to adapter and sets up recyclerView with that argument
        RequestsTabAdapter adapter = new RequestsTabAdapter(getActivity(), Requests);
        recyclerView.setAdapter(adapter);

        return view;

    }

}
