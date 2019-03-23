package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import comnickdchee.github.a3am.Barcode.BarcodeScanner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class NewBookActivity extends AppCompatActivity {
    ProgressDialog pd;
    private static final int CHOSEN_IMAGE = 69;
    private static final int ISBN_READ = 42;
    Uri bookImage;
    ImageView img;
    // text fields that user entered
    private TextInputEditText bookTitleText;
    private TextInputEditText bookAuthorText;
    private TextInputEditText bookISBNText;

    // close or finish activity buttons
    private ImageView closeButton;
    private ImageView addButton;
    private FloatingActionButton cameraButton;


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
                Intent intent = new Intent(NewBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);

                try {
                    URL url = new URL(urlISBN);
                    new JsonTask().execute(urlISBN);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");
        if(requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            bookImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bookImage);
                img.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);
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

            if (bookImage != null) {
                FirebaseUser u = mAuth.getCurrentUser();

                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(bookID);
                bookImageRef.putFile(bookImage);
            }
        } else {
            // TODO: Throw exception here.
        }

    }

    private class JsonTask extends AsyncTask<String, String, ArrayList<String>> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(NewBookActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected ArrayList<String> doInBackground(String... params) {

            JSONObject json = new JSONObject();
            final int n;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                try {
                    json = new JSONObject(buffer.toString());
                    JSONArray items = json.getJSONArray("items");
                    //Log.d("ItemLength: ", "> " + buffer.toString());
                    JSONObject book = items.getJSONObject(0);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String authors = volumeInfo.getString("authors");
                    Log.d("TITLE IS", "doInBackground: " + title);
                    Log.d("AUTHOR IS", "doInBackground: " + authors);
                    ArrayList<String> results = new ArrayList<>();

                    results.add(title);
                    results.add(authors);
                    return results;


                } catch (JSONException e) {
                    Log.e("Failed: ", "> LMAO ");
                    e.printStackTrace();
                }


                } catch (MalformedURLException e) {
                Log.e("MalformedURLException: ", "> ");
                    e.printStackTrace();
                } catch (IOException e) {
                Log.e("IOException: ", "> ");
                    e.printStackTrace();
                } finally {
                Log.d("finally: ", "> ");
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("End: ", "> ");
                return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            super.onPostExecute(result);

            if (pd.isShowing()){
                pd.dismiss();
            }
            Log.d("OnPostExecute: ", "Title = " + result.get(0));
            Log.d("OnPostExecute: ", "Authors = " + result.get(1));
            bookTitleText.setText(result.get(0));
            String authorList = result.get(1).replace("\"","")
                    .replaceAll("\\[","").replaceAll("\\]","")
                    .replaceAll(","," , ");
            bookAuthorText.setText(authorList);
            //String title = json.getString("title");


               //here u ll get whole response...... :-)
        }
    }
}



