package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import comnickdchee.github.a3am.R;

public class ViewOwnedBook extends AppCompatActivity {
    private FirebaseAuth mAuth;
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
        EditText titleBookEditActivity = findViewById(R.id.bookTitleOwnedBook);
        EditText authorBookEditActivity = findViewById(R.id.bookAuthorOwnedBook);
        EditText bookISBNEditActivity = findViewById(R.id.bookISBNOwnedBook);
        bookImageEditActivity = findViewById(R.id.bookPictureOwnedBook);
        bookImageEditActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findImage();
            }
        });
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


    //Helper functions to deal with images (findImage..)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

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

    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE);
    }



}
