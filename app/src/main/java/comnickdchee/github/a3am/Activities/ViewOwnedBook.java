package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    private static final int CHOSEN_IMAGE = 69;

    // UI/View objects
    ImageViewCompat circleImageView;
    private ImageView bookImageEditActivity;
    private ImageView deletePhotoButton;

    // Variables for storing images
    byte[] bArray;
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

        TextInputEditText titleBookEditActivity = findViewById(R.id.tietBookTitle);
        TextInputEditText authorBookEditActivity = findViewById(R.id.tietAuthor);
        TextInputEditText bookISBNEditActivity = findViewById(R.id.tietISBN);
        bookImageEditActivity = findViewById(R.id.ivAddBookPhoto);
        deletePhotoButton = findViewById(R.id.bDeleteImage);

        // Sending intents
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String isbn = intent.getStringExtra("isbn");

        titleBookEditActivity.setText(title);
        authorBookEditActivity.setText(author);
        bookISBNEditActivity.setText(isbn);

        // Disable button if the user has no camera
        if (!hasCamera()) {
            bookImageEditActivity.setEnabled(false);
        }

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");

    //Downloads the data to get it to our initial view
    DatabaseReference ref = database.getReference().child("books").child(key);

    //Downloads the Image and sets it to our image view
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com")
            .child("BookImages").child(key);
    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
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

    //Rig buttons to apply/delete changes.
    ImageView applyChanges = findViewById(R.id.ivFinishAddButton);
    ImageView discardChanges = findViewById(R.id.ivCloseButton);

        applyChanges.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("books").child(key);

            String newTitle = titleBookEditActivity.getText().toString();
            String newAuthor = authorBookEditActivity.getText().toString();
            String newISBN = bookISBNEditActivity.getText().toString();

            databaseReference.child("title").setValue(newTitle);
            databaseReference.child("author").setValue(newAuthor);
            databaseReference.child("isbn").setValue(newISBN);

            if(bookImage != null){
                FirebaseUser u = mAuth.getCurrentUser();

                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(key);
                bookImageRef.putFile(bookImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ViewOwnedBook.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if(bArray != null){
                FirebaseUser u = mAuth.getCurrentUser();

                StorageReference bookImageRef =
                        FirebaseStorage.getInstance().getReference("BookImages").child(key);
                bookImageRef.putBytes(bArray);
            }

            Toast.makeText(ViewOwnedBook.this, "Changes applied to " + newTitle, Toast.LENGTH_LONG).show();

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



}
