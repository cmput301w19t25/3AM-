package comnickdchee.github.a3am.Activities;

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

import comnickdchee.github.a3am.R;

/**
 * @author
 * BorrowedActivity extends AppCompatActivity
 * BorrowedActivity implements View.onClickListener
 * It overrides onCreate and OnClick
 */
public class BorrowedActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText emailReg, passwordReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_borrowed);
        emailReg = (EditText) findViewById(R.id.EmailReg);
        passwordReg = (EditText) findViewById(R.id.PasswordReg);
        findViewById(R.id.RegisterBtn).setOnClickListener(this);
        findViewById(R.id.LoginBtn).setOnClickListener(this);

        if(mAuth.getCurrentUser() != null){

            Intent homePage = new Intent(this, HomepageActivity.class);
            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //homePage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homePage);

        }

    }


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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent i = new Intent(BorrowedActivity.this, HomepageActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterBtn:
                Log.d("Reg", "Ass no error ");
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.LoginBtn:
                Log.d("Login","No error in login");
                UserLogin();
                break;
        }
    }
}
