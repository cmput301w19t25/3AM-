package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class NewBookActivity extends AppCompatActivity {

    // text fields that user entered
    private TextInputEditText bookTitleText;
    private TextInputEditText bookAuthorText;
    private TextInputEditText bookISBNText;

    // close or finish activity buttons
    private ImageView closeButton;
    private ImageView addButton;
    private FloatingActionButton cameraButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        bookTitleText = findViewById(R.id.tietBookTitle);
        bookAuthorText = findViewById(R.id.tietAuthor);
        bookISBNText = findViewById(R.id.tietISBN);

        closeButton = findViewById(R.id.ivCloseButton);
        addButton = findViewById(R.id.ivFinishAddButton);
        cameraButton = findViewById(R.id.fabISBN);

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

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });


    }

    private void addBook(String title, String author, String ISBN) {
        Book newBook = new Book(ISBN, title, author);
        Intent newBookIntent = new Intent();
        newBookIntent.putExtra("NewBook", newBook);
        setResult(Activity.RESULT_OK, newBookIntent);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase fD = FirebaseDatabase.getInstance();
        String str = Integer.toString(java.lang.System.identityHashCode(newBook));
        DatabaseReference dRef = fD.getReference(mAuth.getUid()).child("BooksListID").child(str);
        DatabaseReference bRef = fD.getReference("BooksList").child(str);
        bRef.setValue(newBook);
        dRef.setValue(ISBN);
    }

}
