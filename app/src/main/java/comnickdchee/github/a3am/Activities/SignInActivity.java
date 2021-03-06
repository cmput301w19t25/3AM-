package comnickdchee.github.a3am.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

/**
 * @author Zaheen
 * SignInActivity extends AppCompatActivity
 * SignInActivity implements View.onClickListener
 * It overrides onCreate and OnClick
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText emailReg, passwordReg;
    Backend backend = Backend.getBackendInstance();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);
        emailReg = (EditText) findViewById(R.id.EmailReg);
        passwordReg = (EditText) findViewById(R.id.PasswordReg);
        emailReg.requestFocus();
        findViewById(R.id.RegisterBtn).setOnClickListener(this);
        findViewById(R.id.LoginBtn).setOnClickListener(this);

        //Check if user is logged in, then just load.
        if(mAuth != null && mAuth.getCurrentUser() != null) {
            dialog = new ProgressDialog(SignInActivity.this);
            dialog.setMessage("Loading User Data");
            dialog.show();
            //Log.d("Test", "onCreate: INSIDE");
            Intent homePage = new Intent(this, HomepageActivity.class);
            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            backend.getCurrentUserData(new UserCallback() {
                @Override
                public void onCallback(User user) {
                    startActivity(homePage);
                    finish();

                }
            });
        }

    }

    /*
        Function to check if user login is valid or not and successfully logs in the user if it is.
     */
    private void UserLogin(){
        String email = emailReg.getText().toString().trim();
        String password = passwordReg.getText().toString().trim();

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

        //Get token
        String current_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", current_token);
        //Set Token
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignInActivity.this, HomepageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String current_token = FirebaseInstanceId.getInstance().getToken();
                    databaseReference.child("users").child(mAuth.getUid()).child("device_token").setValue(current_token);

                    backend.getCurrentUserData(new UserCallback() {
                        @Override
                        public void onCallback(User user) {
                            startActivity(intent);
                            finish();

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterBtn:
                Log.d("Register","No error in register");
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.LoginBtn:
                Log.d("Login","No error in login");
                UserLogin();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
