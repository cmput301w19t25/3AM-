package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;

public class RequestBookActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAuthor;
    TextView tvBookTitle;
    TextView tvISBN;
    TextView tvStatus;
    TextView bRequestButton;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
    private Book book;
    Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_book);

        tvAuthor = findViewById(R.id.authorTv);
        tvBookTitle = findViewById(R.id.bookTitleTv);
        tvISBN = findViewById(R.id.ISBNTv);
        tvStatus = findViewById(R.id.statusTv);
        bRequestButton = findViewById(R.id.button);

        // TODO: Make this error free.
        Intent intent = getIntent();
        book = (Book) intent.getExtras().getParcelable("SearchBook");

        tvAuthor.setText(book.getAuthor());
        tvBookTitle.setText(book.getTitle());
        tvISBN.setText(book.getISBN());

        Log.d(book.getBookID(), "onCreate: BookID");

        // Set request button to have an OnClick listener that fires every
        // time the activity gets pressed
        bRequestButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (mAuth.getCurrentUser() != null) {
                    // Once the user requests the current book,
                    // we add them to the array list on the condition
                    // that the current user is not a duplicate, and
                    // we change the view of the button
//                    if (!book.getRequests().contains(mAuth.getCurrentUser().getUid())) {
//                        book.addRequest(mAuth.getCurrentUser().getUid());
//                        updateRequests(book);
                    backend.updateRequests(book);
//                    }

                    // TODO: UI fix; change the request button to be non-clickable.
                }
        }
    }

    /** Overwrite the data of the original book
     with the new book in Firebase */
    private void updateRequests(Book updatedBook) {
        DatabaseReference booksRef = mDatabaseReference.child("books");
        // Change this if its status is available
        if (updatedBook.getStatus().equals(Status.Available)) {
            updatedBook.setStatus(Status.Requested);
        }

        // Update the actual contents in Firebase
        Log.d(updatedBook.getBookID(), "updateRequests: ");
        booksRef.child(updatedBook.getBookID()).setValue(updatedBook);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
