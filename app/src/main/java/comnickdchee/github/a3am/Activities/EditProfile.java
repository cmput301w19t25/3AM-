package comnickdchee.github.a3am.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private String uid;
    private String username;
    private String phone;
    private String email;
    private String address;

    private Toolbar toolbar;
    private CircleImageView usernameIV;
    private TextView usernameTV;
    private EditText emailTV;
    private EditText phoneTV;
    private EditText addressTV;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AlertDialog.Builder passAuthenticate;
    private FirebaseUser user;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri bookImage;
    String key;
    String DownloadLink;
    //String hotfixBookNamesInDrawable;
    private static final int CHOSEN_IMAGE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        uid = intent.getStringExtra("userID");
        username = intent.getStringExtra("username");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        address = intent.getStringExtra("address");

        toolbar = findViewById(R.id.toolbar);
        usernameIV = findViewById(R.id.profilePictureEdit);
        usernameTV = findViewById(R.id.userNameTV);
        emailTV = findViewById(R.id.emailEditText);
        phoneTV = findViewById(R.id.phoneNumberEdit);
        addressTV = findViewById(R.id.addressEdit);
        saveButton = findViewById(R.id.saveButton);

        //Disable button if the user has no camera
        if (!hasCamera()) {
            usernameIV.setEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadImageFromOwnerID(usernameIV, uid);
        usernameTV.setText(username);
        emailTV.setText(email);
        phoneTV.setText(phone);
        addressTV.setText(address);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!email.equals(emailTV.getText().toString()) || !phone.equals(phoneTV.getText().toString())
                        || !address.equals(addressTV.getText().toString())) {
                    if (dataValid()) {
                        createInputPrompt();
                        passAuthenticate.show();
                    }

                } else {
                    finish();
                }

            }
        });
    }

        ////////////////////////////////////////

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
                //findImage();
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //launchs camera
        //Take picture and pass results along to onActivityResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // bookImage = data.getData();
            Log.d("CAMERA VALUE RETURNED", "onActivityResult: ");
            //get photo
            //circleImageView = (CircleImageView) findViewById()
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            usernameIV.setImageBitmap(photo);
        }
    }
    ///////////////////////////////////////////////////////////////end

    public Boolean dataValid() {

        if (addressTV.getText().toString().equals("") || phoneTV.getText().toString().equals("") || emailTV.getText().toString().equals("")) {
            Toast.makeText(EditProfile.this,"Please fill up all the text fields",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    public void createInputPrompt() {
        passAuthenticate = new AlertDialog.Builder(this);
        passAuthenticate.setTitle("Reauthenticate Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passAuthenticate.setView(input);


        passAuthenticate.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                Authenticate(user.getEmail(), password);
            }
        });
        passAuthenticate.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    public void Authenticate (String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    String updatedEmail = emailTV.getText().toString();
                    Log.d(user.getEmail(), "EDIT ACTIVITY: ");
                    Log.d(updatedEmail, "EDIT ACTIVITY: ");
                    user.updateEmail(updatedEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                updateOtherInfo();
                            } else {
                                Toast.makeText(EditProfile.this,"Failed to update email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(EditProfile.this,"Incorrect Password. Authentication failed!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void updateOtherInfo() {
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid);
        mDatabase.child("phoneNumber").setValue(phoneTV.getText().toString());
        mDatabase.child("address").setValue(addressTV.getText().toString());
        mDatabase.child("email").setValue(emailTV.getText().toString());
        Toast.makeText(EditProfile.this,"Changes Saved",Toast.LENGTH_SHORT).show();
        finish();
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
