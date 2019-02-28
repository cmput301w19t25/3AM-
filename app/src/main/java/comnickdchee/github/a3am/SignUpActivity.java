package comnickdchee.github.a3am;

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
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    EditText passwordReg, emailReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //mAuth = FirebaseAuth.getInstance();

        emailReg = (EditText) findViewById(R.id.EmailReg);
        passwordReg = (EditText) findViewById(R.id.PasswordReg);

        findViewById(R.id.RegisterConfirm).setOnClickListener(this);
    }

    private void Register(){
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

        if(password.length() < 6){
            passwordReg.setError("Password needs to be of length greater than 6.");
            passwordReg.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Donkey","Work pls..");
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Successful Registration, login now.", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
                        }
                        // ...
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterConfirm:
                Log.d("Reg", "Ass no error ");
                Register();
            break;
            case R.id.LoginReg:
                startActivity(new Intent(this, BorrowedActivity.class));
                break;
        }
    }
}
