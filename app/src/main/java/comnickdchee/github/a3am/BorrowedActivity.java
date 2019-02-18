package comnickdchee.github.a3am;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class BorrowedActivity extends AppCompatActivity {
    // TODO: Change this so that we get the username from backend
    // and set the action bar's title to it.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed);

        // override the default action bar, allowing us to use
        // action bar commands
        setSupportActionBar((Toolbar) findViewById(R.id.navToolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
