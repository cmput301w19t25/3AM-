package comnickdchee.github.a3am.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
import comnickdchee.github.a3am.Adapters.SearchRecyclerAdapter;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.MySuggestionProvider;
import comnickdchee.github.a3am.R;

public class SearchResultsActivity extends AppCompatActivity {

    private Toolbar navToolbar;
    private RecyclerView rvSearchResults;
    private static final String TAG = "SearchResultsActivity";
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//    private final DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
    private final DatabaseReference booksRef = mFirebaseDatabase.getReference("books");
    private ArrayList<Book> searchResults;
    private ArrayList<Book> titleResults = new ArrayList<>();
    private ArrayList<Book> authorResults = new ArrayList<>();
    private Set<String> bookSet = new HashSet<>();
    private SearchRecyclerAdapter searchResultsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        navToolbar = findViewById(R.id.navToolbar);

        // ArrayList for displaying results
        searchResults = new ArrayList<>();

        rvSearchResults = findViewById(R.id.rvSearchResults);
        searchResultsAdapter = new SearchRecyclerAdapter(this, searchResults);
        rvSearchResults.setAdapter(searchResultsAdapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(navToolbar);

        // Create a back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set the window color to be red
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // Called as soon as the the activity is created
        handleIntent(getIntent());
    }

    /**
     * Called when a new intent is passed. Note that is a "androidTop" activity,
     * meaning that the intents are passed recursively.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles the action where the user presses enter on a search query.
     * We use a set that hashes on the key of the particular snapshotted book.
     * If the key exists, we don't add the book, otherwise we do.
     */
    private void handleIntent(Intent intent) {

        searchResults.clear();
        bookSet.clear();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Save the most recent query for later search suggestion
            String query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();

            // Parse string using whitespaces as delimiter
            String[] queryList = query.split(" ");

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // Brute Force Search: Use the books table as our starting reference,
            // iterate through all child entries to get their information, then
            // make a comparison with the query entered
            booksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through child entries
                    searchResults.clear();
                    bookSet.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        // Get all the keywords in the query entered by the user
                        // and make a direct check
                        Boolean addBook = true;
                        Book bookToCompare = data.getValue(Book.class);


                        for (String keyword : queryList) {

                            keyword = keyword.replace(" ", "");


                            if (keyword.equals("")) {
                                continue;
                            }

                            if (bookToCompare != null) {
                                bookToCompare.setBookID(data.getKey());


                                // Save the bookID to fetch intent information

                                keyword = " " + keyword + " ";
                                String title = " " + bookToCompare.getTitle().toLowerCase() + " ";
                                String author = " " + bookToCompare.getAuthor().toLowerCase() + " ";
                                String ISBN = " " + bookToCompare.getISBN().toLowerCase() + " ";
                                if (!title.contains(keyword) &&
                                        !author.contains(keyword) &&
                                        !ISBN.contains(keyword)) {

                                    addBook = false;
                                }
                            }
                            else {
                                break;
                            }
                        }
                        if (addBook){
                            searchResults.add(bookToCompare);
                        }

                    }

                    searchResultsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });



        }

    }

    /**
     * Inflate the menu when the SearchView item is pressed with the
     * search menu. The toolbar gets a search view button implemented
     * on the right side.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

        return true;
    }

    /** Overriding options item navigating back to the homepage activity. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
