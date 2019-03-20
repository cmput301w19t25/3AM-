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
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import comnickdchee.github.a3am.Adapters.BookRecyclerAdapter;
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
    private ArrayList<Book> titleResults;
    private ArrayList<Book> authorResults;
    private Set<Book> bookSet = new HashSet<>();
    private BookRecyclerAdapter searchResultsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        navToolbar = findViewById(R.id.navToolbar);

        searchResults = new ArrayList<>();

        rvSearchResults = findViewById(R.id.rvSearchResults);
        searchResultsAdapter = new BookRecyclerAdapter(this, searchResults);
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

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Save the most recent query for later search suggestion
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // Perform search with the given query
            Query queryRefTitle = booksRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");
            Query queryRefAuthor = booksRef.orderByChild("author").startAt(query).endAt(query + "\uf8ff");

            // Attach a listener to perform search on title
            queryRefTitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    searchResults.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Book book = data.getValue(Book.class);
                        searchResults.add(book);

                        searchResultsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            // Attach a listener to perform search on author
            queryRefAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    searchResults.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Book book = data.getValue(Book.class);
                        searchResults.add(book);

                        searchResultsAdapter.notifyDataSetChanged();
                    }
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

}
