package comnickdchee.github.a3am;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import comnickdchee.github.a3am.Models.Book;

public class MyBooksFragment extends Fragment {

    private ArrayList<String> bookList;
    private RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        Bundle args = getArguments();

        FloatingActionButton fab = view.findViewById(R.id.fabAddBookButton);

        /**
         * Passes intent to the NewBook activity, using StartActivityForResult
         * to get back the results of an activity.
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewBookActivity.class);

                // using this to get back the results of an intent
                // Source: https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                startActivityForResult(intent, 1);

            }
        });

        bookList = new ArrayList<String>();
        bookList.add("Hawwy Potta and the Prisoner Of Afghanistan");
        bookList.add("Hawwy Potta and the Sorcerer's Stoned");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(), bookList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    /**
     * Fired after Add Book Activity has finished.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Book book = (Book) data.getSerializableExtra("NewBook");
            bookList.add(book.getTitle());
            adapter.notifyDataSetChanged();
        }
    }
}
