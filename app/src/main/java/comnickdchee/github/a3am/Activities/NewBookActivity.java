package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewBookActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    ProgressDialog pd;
    private static final int CHOSEN_IMAGE = 69;
    private static final int ISBN_READ = 42;
    Uri bookImage;
    ImageView img;
    Bitmap photo;
    byte[] bArray;
    // text fields that user entered
    private TextInputEditText bookTitleText;
    private TextInputEditText bookAuthorText;
    private TextInputEditText bookISBNText;

    private Backend backend = Backend.getBackendInstance();

    // close or finish activity buttons
    private ImageView closeButton;
    private ImageView addButton;
    private ImageView deletePhotoButton;
    private FloatingActionButton cameraButton;

    private ProgressBar mProgressbar;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_PERMISSION_CODE = 10;

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
        mProgressbar = findViewById(R.id.progressBar2);

        closeButton = findViewById(R.id.ivCloseButton);
        addButton = findViewById(R.id.ivFinishAddButton);
        cameraButton = findViewById(R.id.fabISBN);
        deletePhotoButton = findViewById(R.id.bDeleteImage);
        deletePhotoButton.setVisibility(View.GONE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(NewBookActivity.this, "One or mor fields are empty", Toast.LENGTH_LONG).show();
                // get all the desired inputs from the user
                if (isEmpty(bookTitleText) || isEmpty(bookAuthorText) || isEmpty(bookISBNText)) {
                    Toast.makeText(NewBookActivity.this, "One or more fields are empty", Toast.LENGTH_LONG).show();

                } else{
                    String bookTitle = bookTitleText.getText().toString();
                    String bookAuthor = bookAuthorText.getText().toString();
                    String bookISBN = bookISBNText.getText().toString();
                    addBook(bookTitle, bookAuthor, bookISBN);
                    finish();
                }

            }
        });

        //set onclick listener on camera button
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ISBN : 9780773547971 --> Should give book title : "Promise and Challenge of Party Primary Elections"

                Intent intent = new Intent(NewBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);



            }
        });

        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findImage();
            }
        });*/


    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //shows pop up
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.photo_menu);
        popup.show();

    }

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

    //Check if the user has camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //launching the camera
    public void launchCamera(View view) {
        if (ContextCompat.checkSelfPermission(NewBookActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Here we can write if we need the camera to do anything extra if we already have permission


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //launchs camera
            //Take picture and pass results along to onActivityResult
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }
    static final int REQUEST_TAKE_PHOTO = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Here we can write if we need the camera to do anything extra if we get the permission
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //launchs camera
                //Take picture and pass results along to onActivityResult
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        bookImage = FileProvider.getUriForFile(this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, bookImage);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }


            } else {
                Toast.makeText(this,"Permission Denied for Camera",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
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
            String urlISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            try {
                URL url = new URL(urlISBN);
                new JsonTask().execute(urlISBN);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            bookISBNText.setText(isbn);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            bookImage = getIntent().getData();
            Log.d("CAMERA VALUE RETURNED", "onActivityResult: ");
            //get photo
            //circleImageView = (CircleImageView) findViewById()
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            //bookImage = getImageUri(this, photo);
            //Set firebase here.
            Bitmap yourBitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bArray = bos.toByteArray();

            //Log.d("Photo", bookImage.toString());
            img.setImageBitmap(photo);
        }
    }

    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE);
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    /** Adds book to both the database and user class. */
    private void addBook(String title, String author, String ISBN) {
        if (mAuth.getCurrentUser() != null) {
            String userKey = mAuth.getCurrentUser().getUid();
            Book newBook = new Book(ISBN, title, author, userKey);
            backend.addBook(newBook);
            //Log.d("Bookimage", bookImage.toString());
            if(bookImage!= null){
                setResult(RESULT_OK);
                FirebaseUser u = mAuth.getCurrentUser();

                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(newBook.getBookID());
                bookImageRef.putFile(bookImage);
            }
            if(bArray != null){

                setResult(RESULT_OK);
                FirebaseUser u = mAuth.getCurrentUser();

                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(newBook.getBookID());
                bookImageRef.putBytes(bArray);
            }

        }
    }

    /**
     * Method responsible for adding book to Firebase database, both under the
     * owned_books nested table inside the current user's table entry, as well as
     * the actual books table.
     */
//    private void addBookToDatabase(Book book) {
//        if (mAuth.getCurrentUser() != null) {
//==            String uid = mAuth.getCurrentUser().getUid();
//
//            // Generate a bookID for the newly created book
//            // and add to the user's table
//            DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
//            String bookID = usersRef.child(uid).child("ownedBooks").push().getKey();
//            usersRef.child(uid).child("ownedBooks").push().setValue(bookID);
//
//            // Then, we add the book to the book table with all of its proper parameters
//            mFirebaseDatabase.getReference("books").child(bookID).setValue(book);
//
//            if (bookImage != null) {
//                FirebaseUser u = mAuth.getCurrentUser();
//
//                StorageReference bookImageRef =
//                        FirebaseStorage.getInstance().getReference("BookImages").child(bookID);
//                bookImageRef.putFile(bookImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mProgressbar.setProgress(0);
//                            }
//                        }, 5000);
//                        Toast.makeText(NewBookActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(NewBookActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        mProgressbar.setVisibility(View.VISIBLE);
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        mProgressbar.setProgress((int)(progress));
//                    }
//                });
//            }
//        } else {
//            // TODO: Throw exception here.
//        }
//
//    }

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
                Toast.makeText(NewBookActivity.this, "No books found with this ISBN", Toast.LENGTH_SHORT).show();
            }

            //String title = json.getString("title");


               //here u ll get whole response...... :-)
        }
    }

    /**
     * For checking if required text fields are left blank.
     */
    private boolean isEmpty(TextInputEditText tietText) {
        return (tietText.getText().toString().matches(""));
    }
}



