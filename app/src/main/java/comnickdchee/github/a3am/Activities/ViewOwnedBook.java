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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import comnickdchee.github.a3am.R;

public class ViewOwnedBook extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView bookImageEditActivity;
    Uri bookImage;
    String DownloadLink;
    private static final int CHOSEN_IMAGE = 69;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_owned_book);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Gets Current User
        mAuth = FirebaseAuth.getInstance();
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
        String key = bundle.getString("key");

        DatabaseReference ref = database.getReference().child("books").child(key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String TitleBook = dataSnapshot.child("title").getValue().toString();
                String AuthorBook = dataSnapshot.child("author").getValue().toString();
                String ISBN = dataSnapshot.child("isbn").getValue().toString();

                titleBookEditActivity.setText(TitleBook);
                authorBookEditActivity.setText(AuthorBook);
                bookISBNEditActivity.setText(ISBN);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    }



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
