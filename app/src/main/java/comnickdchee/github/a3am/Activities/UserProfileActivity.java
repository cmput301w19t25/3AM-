package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author cmput301w19t25
 * This class extends AppCompatActivity
 * @see AppCompatActivity
 */
public class UserProfileActivity extends AppCompatActivity {

    ImageView backButton;
    Button messageButton;
    CircleImageView userPhoto;
    TextView userName;
    TextView userPhone;
    TextView userEmail;
    TextView userAddress;
    String userID;
    Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // To disable unused views and buttons for this activity
        CardView bookCard = findViewById(R.id.bookCard);
        Button transactionButton = findViewById(R.id.transactionButton);
        bookCard.setVisibility(View.INVISIBLE);
        transactionButton.setVisibility(View.INVISIBLE);

        // Get's data pushed into this intent
        Intent intent = getIntent();
        userID = intent.getStringExtra("key");


        // Setting the user Details
        userPhoto = findViewById(R.id.UserImage);
        userName = findViewById(R.id.usernameTV);
        userPhone = findViewById(R.id.phoneTV);
        userEmail = findViewById(R.id.emailTV);
        userAddress = findViewById(R.id.tvAddress);

        backend.getUser(userID, new UserCallback() {
            @Override
            /**
             * This method is used to set the user's info. i.e; phoneNum, email, etc
             */
            public void onCallback(User user) {
                userName.setText(user.getUserName());
                userPhone.setText(user.getPhoneNumber());
                userEmail.setText(user.getEmail());
                userAddress.setText(user.getAddress());
                loadImageFromOwnerID(userPhoto,userID);
            }
        });



        // Setting the buttons
        backButton = findViewById(R.id.backIV);
        messageButton = findViewById(R.id.message);

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is used to set an onClick listener on the back arrow button
             * @param view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        messageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is used to set an onClick listener on message button that opens the messaging
             * screen when clicked.
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), messageActivity.class);
                i.putExtra("key", userID);
                startActivity(i);
            }
        });

    }

    /**
     * This method is used to load the profile picture of the owner of the book.
     * @param load
     * @param uID
     */
    public void loadImageFromOwnerID(ImageView load, String uID){

        // This function takes a imageView and loads the profile picture of user with userID of uID to the imageView

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
