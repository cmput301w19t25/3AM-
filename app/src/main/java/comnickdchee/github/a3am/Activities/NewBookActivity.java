package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

public class NewBookActivity extends AppCompatActivity {
    private static final int CHOSEN_IMAGE = 69;
    Uri bookImage;
    ImageView img;
    // text fields that user entered
    private TextInputEditText bookTitleText;
    private TextInputEditText bookAuthorText;
    private TextInputEditText bookISBNText;

    // Firebase references to use for saving to database
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        bookTitleText = findViewById(R.id.tietBookTitle);
        bookAuthorText = findViewById(R.id.tietAuthor);
        bookISBNText = findViewById(R.id.tietISBN);
        img = (ImageView) findViewById(R.id.ivAddBookPhoto);

        ImageView closeButton = findViewById(R.id.ivCloseButton);
        ImageView addButton = findViewById(R.id.ivFinishAddButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get all the desired inputs from the user
                String bookTitle = bookTitleText.getText().toString();
                String bookAuthor = bookAuthorText.getText().toString();
                String bookISBN = bookISBNText.getText().toString();
                addBook(bookTitle, bookAuthor, bookISBN);
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findImage();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            bookImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bookImage);
                img.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE);
    }

    /** Adds book to both the database and user class. */
    private void addBook(String title, String author, String ISBN) {
        Book newBook = new Book(ISBN, title, author);
        //Intent newBookIntent = new Intent();
        //newBookIntent.putExtra("NewBook", newBook);
        //setResult(Activity.RESULT_OK, newBookIntent);
        addBookToDatabase(newBook);
    }

    /**
     * Method responsible for adding book to Firebase database, both under the
     * owned_books nested table inside the current user's table entry, as well as
     * the actual books table.
     */
    private void addBookToDatabase(Book book) {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();

            // Generate a bookID for the newly created book
            // and add to the user's table
            DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
            String bookID = usersRef.child(uid).child("owned_books").push().getKey();
            usersRef.child(uid).child("owned_books").push().setValue(bookID);

            // Then, we add the book to the book table with all of its proper parameters
            mFirebaseDatabase.getReference("books").child(bookID).setValue(book);

        } else {
            // TODO: Throw exception here.
        }

    }

}
