package comnickdchee.github.a3am;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import comnickdchee.github.a3am.Models.User;

import static java.lang.Boolean.TRUE;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CHOSEN_IMAGE = 69;
    ImageView img;
    Uri ProfileImage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase Fd;
    private FirebaseStorage firebaseStorage;
    Boolean flag;
    EditText passwordReg, emailReg, userName, address, phoneNumber;
    String userN, email, password, addR, phoneN, profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        emailReg = (EditText) findViewById(R.id.EmailReg);
        passwordReg = (EditText) findViewById(R.id.PasswordReg);
        userName = (EditText) findViewById(R.id.userName);
        address = (EditText) findViewById(R.id.address);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        findViewById(R.id.RegisterConfirm).setOnClickListener(this);
        findViewById(R.id.AddProfilePicture).setOnClickListener(this);
        findViewById(R.id.userImage).setOnClickListener(this);
        img = (ImageView) findViewById(R.id.userImage);
    }

    private void Register(){
        userN = userName.getText().toString().trim();
        email = emailReg.getText().toString().trim();
        password = passwordReg.getText().toString().trim();
        addR = address.getText().toString().trim();
        phoneN = phoneNumber.getText().toString().trim();

        if(userN.isEmpty()){
            userName.setError("User Name Required!");
            userName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailReg.setError("Need an email!");
            emailReg.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailReg.setError("Valid email only.");
            emailReg.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordReg.setError("Password Required");
            passwordReg.requestFocus();
            return;
        }

        if(userN.length() > 25){
            userName.setError("Username too Long!");
            userName.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordReg.setError("Password needs to be of length greater than 6.");
            passwordReg.requestFocus();
            return;
        }


        if(addR.isEmpty()){
            address.setError("Address Required!");
            address.requestFocus();
            return;
        }else{
            Log.d("Address", addR.toString());
        }


        if(phoneN.isEmpty()){
            phoneNumber.setError("Phone Number Required!");
            phoneNumber.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserData();
                            Log.d("Donkey","Work pls..");
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Successful Registration.", Toast.LENGTH_LONG).show();
                            uploadImageToFirebase();
                            mAuth.signOut();
                            finish();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "Already registered with this email!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        // ...
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.AddProfilePicture:
                Log.d("Image","Image clicked");
                findImage();
            case R.id.userImage:
                Log.d("Image","Image clicked");
                findImage();
            case R.id.RegisterConfirm:
                Log.d("Reg", "Ass no error ");
                Register();
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            ProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ProfileImage);
                img.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void findImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"), CHOSEN_IMAGE) ;
    }

    private void sendUserData(){
        FirebaseDatabase fD = FirebaseDatabase.getInstance();
        User newUser = new User(userN,email,addR,phoneN);

        DatabaseReference mRef = fD.getReference(mAuth.getUid());
        mRef.setValue(newUser);
        //mRef.setValue(f);

    }

    private void uploadImageToFirebase(){
        if(ProfileImage != null){
            StorageReference profileImageRef =
                    FirebaseStorage.getInstance().getReference(email+"/"+"dp"+ ".jpg");
            profileImageRef.putFile(ProfileImage);
            profileImageUrl = profileImageRef.getDownloadUrl().toString();
            }

            FirebaseUser u = mAuth.getCurrentUser();

        if(u != null && profileImageUrl != null){
            Log.d("Image","Got profile image data");
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userN)
                    .setPhotoUri(Uri.parse(profileImageUrl)).build();
            Log.d("Image", profileImageUrl.toString());
            u.updateProfile(profile);
        }else{
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userN).build();
            u.updateProfile(profile);
        }
    }
}
