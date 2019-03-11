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

import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Adapters.RecyclerViewAdapter;
import comnickdchee.github.a3am.Fragments.BorrowedFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;

/**
 * @author Nicholas
 * MyBooksFragment extends Fragment
 * it overwrites onCreateView
 */
public class MyBooksFragment extends Fragment {

    private ArrayList<Book> BookList = new ArrayList<>();
    private BookRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Opens a fragment which will show the books
        View view = inflater.inflate(R.layout.fragment_books, container, false);
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

        // Initializes some sample data to be displayed
        Book book1 = new Book("11211323","Hawwy Potta and the Prisoner Of Afghanistan","Just Kidding Rowling");
        Book book2 = new Book("12211323","Hawwy Potta and the Sorcerer's Stoned","Just Kidding Rowling");
        BookList.add(book1);
        BookList.add(book2);

        User user1 = new User("nchee","nchee@mom.ca","China","1234556");

        book2.setCurrentBorrower(user1);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new BookRecyclerAdapter(getActivity(), BookList);
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
            BookList.add(book);
            adapter.notifyDataSetChanged();
        }
    }
}
