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

import comnickdchee.github.a3am.Adapters.RequestsTabAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.RequestStatusGroup;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private ArrayList<RequestStatusGroup> Requests = new ArrayList<>();
    private ArrayList<Book> requestList = new ArrayList<>();
    private Backend backend = Backend.getBackendInstance();
    private TextView noDataView;

    /** Required empty constructor since its a fragment */
    public RequestsFragment() {
        // Required empty public constructor
    }

    /** Creates the view by getting the respective book list and calling the requests tab adapter */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);
        Log.d("LOL", "onCreateView: Request Fragment Created");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noDataView = view.findViewById(R.id.noDataView);

        recyclerView.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.INVISIBLE);

        // Passes argument to adapter and sets up recyclerView with that argument
        RequestsTabAdapter adapter = new RequestsTabAdapter(getActivity(), Requests);
        recyclerView.setAdapter(adapter);

        // gets the data from backend
        backend.getRequestedBooks(new BookListCallback() {
            @Override
            public void onCallback(ArrayList<Book> books) {
                Requests.clear();
                requestList.clear();
                requestList.addAll(0,books);

                ArrayList<Book> AcceptedRequests = new ArrayList<>();
                ArrayList<Book> pendingRequests = new ArrayList<>();

                for (int i = 0; i < requestList.size(); i++) {
                    if (requestList.get(i).getStatus() == Status.Accepted) {
                        AcceptedRequests.add(requestList.get(i));
                    } else {
                        pendingRequests.add(requestList.get(i));
                    }
                }

                // Adding those request to the AcceptedGroup (The First argument determines the name of the Group)
                RequestStatusGroup AcceptedGroup = new RequestStatusGroup("Accepted", AcceptedRequests);
                Requests.add(AcceptedGroup);

                // Adding those request to the PendingGroup (The First argument determines the name of the Group)
                RequestStatusGroup PendingGroup = new RequestStatusGroup("Pending", pendingRequests);
                Requests.add(PendingGroup);

                RequestsTabAdapter newAdapter = new RequestsTabAdapter(getActivity(), Requests);
                recyclerView.setAdapter(newAdapter);
            }
        });

        return view;

    }

}
