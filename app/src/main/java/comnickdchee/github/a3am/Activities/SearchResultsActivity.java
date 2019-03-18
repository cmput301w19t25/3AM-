package comnickdchee.github.a3am.Activities;

import android.app.SearchManager;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;

import comnickdchee.github.a3am.MySuggestionProvider;
import comnickdchee.github.a3am.R;

public class SearchResultsActivity extends AppCompatActivity {

    private Toolbar navToolbar;
    private static final String TAG = "SearchResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: It launched!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        navToolbar = findViewById(R.id.navToolbar);
        setSupportActionBar(navToolbar);
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
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

    }

}
