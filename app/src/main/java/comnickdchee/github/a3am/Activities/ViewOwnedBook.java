package comnickdchee.github.a3am.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import comnickdchee.github.a3am.R;

public class ViewOwnedBook extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Gets Current User
        mAuth = FirebaseAuth.getInstance();

        EditText titleBookEditActivity = findViewById(R.id.bookTitleOwnedBook);
        EditText authorBookEditActivity = findViewById(R.id.bookAuthorOwnedBook);
        EditText bookISBNEditActivity = findViewById(R.id.bookISBNOwnedBook);

    }


}
