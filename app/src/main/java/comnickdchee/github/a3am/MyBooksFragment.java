package comnickdchee.github.a3am;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RecyclerViewAdapter;
import comnickdchee.github.a3am.Fragments.BorrowedFragment;

public class MyBooksFragment extends Fragment {

    private ArrayList<String> BookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        Bundle args = getArguments();
        ArrayList<String> data = new ArrayList<>();
        data.add("Hawwy Potta and the Prisoner Of Afghanistan");
        data.add("Hawwy Potta and the Sorcerer's Stoned");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),data);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
