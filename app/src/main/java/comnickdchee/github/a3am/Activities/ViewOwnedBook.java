package comnickdchee.github.a3am.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewOwnedBook extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private FirebaseAuth mAuth;
    ImageViewCompat circleImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_PERMISSION_CODE = 10;

    ImageView bookImageEditActivity;
    Uri bookImage;
    String key;
    String DownloadLink;
    //String hotfixBookNamesInDrawable;
    private static final int CHOSEN_IMAGE = 69;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Gets Current User
        mAuth = FirebaseAuth.getInstance();

        //FIRST WE SET ALL OUR DATA TO THE EDIT BOOK PAGE.
        TextInputLayout titleBookEditActivity = findViewById(R.id.bookTitleOwnedBook);
        TextInputLayout authorBookEditActivity = findViewById(R.id.bookAuthorOwnedBook);
        TextInputLayout bookISBNEditActivity = findViewById(R.id.bookISBNOwnedBook);
        bookImageEditActivity = findViewById(R.id.bookPictureOwnedBook);
        //circleImageView = (ImageViewCompat) findViewById(R.id.bookPictureOwnedBook);

        //Disable button if the user has no camera
        if (!hasCamera()) {
            bookImageEditActivity.setEnabled(false);
        }

        Bundle bundle = getIntent().getExtras();
    key = bundle.getString("key");
        Log.d(key, "keyReceivedViewBooks: ");

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

    //Rig buttons to apply/delete changes.
    TextView applyChanges = findViewById(R.id.ApplyChangesEditBook);
    TextView discardChanges = findViewById(R.id.DiscardChangesEditBook);

        applyChanges.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("books").child(key);

            String newTitle = titleBookEditActivity.getEditText().toString();
            String newAuthor = authorBookEditActivity.getEditText().toString();
            String newISBN = bookISBNEditActivity.getEditText().toString();

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
            } else {
                Log.d("", "onClick: ");
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

    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE);
    }

    //Check if the user has camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //launching the camera
    public void launchCamera(View view) {
        if (ContextCompat.checkSelfPermission(ViewOwnedBook.this,
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

    //if you want to return image taken


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            bookImage = data.getData();
            Log.d("CAMERA VALUE RETURNED", "onActivityResult: ");
            //get photo
            //circleImageView = (CircleImageView) findViewById()
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            bookImageEditActivity.setImageBitmap(photo);
        }

        if(requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            bookImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bookImage);
                bookImageEditActivity.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }



}
