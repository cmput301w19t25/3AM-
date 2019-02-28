package comnickdchee.github.a3am.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import comnickdchee.github.a3am.R;

public class HomepageActivity extends AppCompatActivity {
    private Toolbar navToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        navToolbar = findViewById(R.id.navToolbar);
        setSupportActionBar(navToolbar);
    }
}
