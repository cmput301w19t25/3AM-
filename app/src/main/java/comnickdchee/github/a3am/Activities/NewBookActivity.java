package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
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

import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Barcode.GoogleAPIBooks;
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

import org.json.JSONException;
import org.json.JSONObject;

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
                //ISBN : 9780773547971 --> Should give book title : "Promise and Challenge of Party Primary Elections"
                String isbn = "9780773547971";
                String urlISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                try {
                    URL url = new URL(urlISBN);
                    JSONObject = Jsonfrom

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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

    public class GetDataSync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                getData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtUser.setText(saldo);
        }
    }

    private void getData() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("http://piggybank.wordmediavormgever.nl/getSaldo.php");
        try {
            String response = json.getString("saldo");
            Log.e("AAAAAAAAA %s", response);
            saldo = response;

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

}
