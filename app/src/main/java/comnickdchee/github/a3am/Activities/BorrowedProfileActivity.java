package comnickdchee.github.a3am.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;

public class BorrowedProfileActivity extends AppCompatActivity {

    ImageView backButton;
    Button transactionButton;
    private static final int ISBN_READ = 42;
    private Book actionBook;
    private Context mContext;
    private Backend backend = Backend.getBackendInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        setContentView(R.layout.activity_user_profile);


        backButton = findViewById(R.id.backIV);
        transactionButton = findViewById(R.id.transactionButton);
        transactionButton.setText("Confirm Return");

        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("passedBook");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowedProfileActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);

            //TODO: DELETE EXCHANGE
            String bookISBN = actionBook.getISBN();

            if (isbn.equals(bookISBN)) {
                actionBook.setStatus(Status.Available);
                backend.updateBookData(actionBook);
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }
        }
    }
}