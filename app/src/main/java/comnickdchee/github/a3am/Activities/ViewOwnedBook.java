package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.BookCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;

import comnickdchee.github.a3am.R;

/**
 * ViewOwnedBook Activity that lets the user view the current book
 * in the my books fragment, letting them edit and make changes
 * to the book and save it to the database.
 */
public class ViewOwnedBook extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    // Firebase verification
    private FirebaseAuth mAuth;

    // Flags for intents
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_PERMISSION_CODE = 10;
    private Backend backend = Backend.getBackendInstance();

    private static final int ISBN_READ = 42;

    byte[] bArray;
    private ImageView bookImageEditActivity;
    private ImageView deletePhotoButton;

    private TextInputEditText bookTitleText;
    private TextInputEditText bookAuthorText;
    private TextInputEditText bookISBNText;

    private FloatingActionButton cameraButton;

    ProgressDialog pd;

    private static final int CHOSEN_IMAGE = 69;

    Uri bookImage;
    String key;
    String DownloadLink;

    /**
     * Method fires when the activity is created and populates the respective fields
     * with their appropriate data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Gets current user
        mAuth = FirebaseAuth.getInstance();

        // Get all the current book data from the my books fragment adapter
        Intent intent = getIntent();

        // Changing view based on activity
        TextView ActivityTitle = findViewById(R.id.tvTitle);
        ActivityTitle.setText("Edit Book");

        bookTitleText = findViewById(R.id.tietBookTitle);
        bookAuthorText = findViewById(R.id.tietAuthor);
        bookISBNText = findViewById(R.id.tietISBN);
        bookImageEditActivity = findViewById(R.id.ivAddBookPhoto);
        deletePhotoButton = findViewById(R.id.bDeleteImage);
        cameraButton = findViewById(R.id.fabISBN);

        // Sending intents
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String isbn = intent.getStringExtra("isbn");

        bookTitleText.setText(title);
        bookAuthorText.setText(author);
        bookISBNText.setText(isbn);

        // Disable button if the user has no camera
        if (!hasCamera()) {
            bookImageEditActivity.setEnabled(false);
        }

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");

        //Downloads the data to get it to our initial view.
        DatabaseReference ref = database.getReference().child("books").child(key);

        //Downloads the Image and sets it to our image view.
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(key);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                Picasso.with(getApplicationContext()).load(DownloadLink).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(bookImageEditActivity);
            }
        });

        // Delete the photo attached to the book, where key is the bookID
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(key);
                bookImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bookImageEditActivity.setImageResource(R.drawable.ic_add_to_photos_grey_24dp);
                    }
                });
            }

        });

        // This opens the ISBN camera when the camera button is pressed
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ISBN : 9780773547971 --> Should give book title : "Promise and Challenge of Party Primary Elections"

                Intent intent = new Intent(ViewOwnedBook.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

        //Rig buttons to apply/delete changes.
        ImageView applyChanges = findViewById(R.id.ivFinishAddButton);
        ImageView discardChanges = findViewById(R.id.ivCloseButton);

            applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("books").child(key);

                if(bookImage != null){
                    FirebaseUser u = mAuth.getCurrentUser();

                    StorageReference bookImageRef =
                            FirebaseStorage.getInstance().getReference("BookImages").child(key);
                    bookImageRef.putFile(bookImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            backend.getSingleBook(key, new BookCallback() {
                                @Override
                                public void onCallback(Book book)
                                {
                                    String newTitle = bookTitleText.getText().toString();
                                    String newAuthor = bookAuthorText.getText().toString();
                                    String newISBN = bookISBNText.getText().toString();

                                    book.setTitle(newTitle);
                                    book.setAuthor(newAuthor);
                                    book.setISBN(newISBN);

                                    String owner = book.getOwnerID();

                                    book.setOwnerID(owner + " ");
                                    backend.updateBookData(book);

                                    book.setOwnerID(owner);
                                    backend.updateBookData(book);
                                }
                            });
                        }
                    });
                }
                else if(bArray != null){
                    FirebaseUser u = mAuth.getCurrentUser();

                    StorageReference bookImageRef =
                            FirebaseStorage.getInstance().getReference("BookImages").child(key);
                    bookImageRef.putBytes(bArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            backend.getSingleBook(key, new BookCallback() {
                                @Override
                                public void onCallback(Book book)
                                {
                                    String newTitle = bookTitleText.getText().toString();
                                    String newAuthor = bookAuthorText.getText().toString();
                                    String newISBN = bookISBNText.getText().toString();

                                    book.setTitle(newTitle);
                                    book.setAuthor(newAuthor);
                                    book.setISBN(newISBN);

                                    String owner = book.getOwnerID();

                                    book.setOwnerID(owner + " ");
                                    backend.updateBookData(book);

                                    book.setOwnerID(owner);
                                    backend.updateBookData(book);
                                }
                            });
                        }
                    });
                } else {
                    backend.getSingleBook(key, new BookCallback() {
                        @Override
                        public void onCallback(Book book)
                        {
                            String newTitle = bookTitleText.getText().toString();
                            String newAuthor = bookAuthorText.getText().toString();
                            String newISBN = bookISBNText.getText().toString();

                            book.setTitle(newTitle);
                            book.setAuthor(newAuthor);
                            book.setISBN(newISBN);

                            backend.updateBookData(book);
                        }
                    });
                }

                finish();

            }
        });
            discardChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewOwnedBook.this, "Changes discarded to book.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /** Creates a popup menu for the user to take a photo or select a photo from the gallery. */
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.photo_menu);
        popup.show();

    }

    /**
     * Handles the menu logic for when user chooses to take a photo or
     * get a photo from their gallery.
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
                launchCamera(findViewById(android.R.id.content));
                return true;

            case R.id.item2:
                Toast.makeText(this, "Gallery clicked", Toast.LENGTH_SHORT).show();
                findImage();
                return true;
        }
        return false;
    }

    /** Finds image from gallery */
    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE);
    }

    /** Method that checks if the user has a camera. */
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /** Method that launches the camera */
    public void launchCamera(View view) {
        if (ContextCompat.checkSelfPermission(ViewOwnedBook.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            // Here we can write if we need the camera to do anything extra if we already have permission
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //launches camera

            // Take picture and pass results along to onActivityResult
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }

    /**
     * Check the result of whether or not the user allowed the app permission to
     * use their camera.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Here we can write if we need the camera to do anything extra if we get the permission
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //launchs camera
                //Take picture and pass results along to onActivityResult
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this,"Permission Denied for Camera",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }


    /** Overriden method that handles the logic for whether or not the book photo was taken. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            bookImage = data.getData();
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bArray = bos.toByteArray();

            bookImageEditActivity.setImageBitmap(photo);
            bookImageEditActivity.setImageURI(bookImage);

        }

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);
            String urlISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            try {
                URL url = new URL(urlISBN);
                new ViewOwnedBook.JsonTask().execute(urlISBN);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            bookISBNText.setText(isbn);
        }

        // Check if we got back an image from the camera
        if(requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            bookImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bookImage);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bArray = bos.toByteArray();

                bookImageEditActivity.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private class JsonTask extends AsyncTask<String, String, ArrayList<String>> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewOwnedBook.this);
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
                    ArrayList<String> results = new ArrayList<>();
                    json = new JSONObject(buffer.toString());
                    JSONArray items = json.getJSONArray("items");
                    if (json.isNull("items")) {
                        return results;
                    }
                    //Log.d("ItemLength: ", "> " + buffer.toString());
                    JSONObject book = items.getJSONObject(0);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String authors = volumeInfo.getString("authors");
                    Log.d("TITLE IS", "doInBackground: " + title);
                    Log.d("AUTHOR IS", "doInBackground: " + authors);


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
            if (result != null){
                bookTitleText.setText(result.get(0));
                String authorList = result.get(1).replace("\"","")
                        .replaceAll("\\[","").replaceAll("\\]","")
                        .replaceAll(","," , ");
                bookAuthorText.setText(authorList);
            }
            else
            {
                Toast.makeText(ViewOwnedBook.this, "No books found with this ISBN", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
