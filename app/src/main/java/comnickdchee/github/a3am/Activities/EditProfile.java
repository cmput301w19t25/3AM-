package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private String uid;
    private String username;
    private String phone;
    private String email;
    private String address;

    private CircleImageView usernameIV;
    private TextView usernameTV;
    private EditText emailTV;
    private EditText phoneTV;
    private EditText addressTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        uid = intent.getStringExtra("userID");
        username = intent.getStringExtra("username");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        address = intent.getStringExtra("address");

        usernameIV = findViewById(R.id.profilePictureEdit);
        usernameTV = findViewById(R.id.userNameTV);
        emailTV = findViewById(R.id.emailEditText);
        phoneTV = findViewById(R.id.phoneNumberEdit);
        addressTV = findViewById(R.id.addressEdit);

        loadImageFromOwnerID(usernameIV,uid);
        usernameTV.setText(username);
        emailTV.setText(email);
        phoneTV.setText(phone);
        addressTV.setText(address);

    }

    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }


}
