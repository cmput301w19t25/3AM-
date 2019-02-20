package comnickdchee.github.a3am;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText passwordReg, emailReg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailReg = (EditText) findViewById(R.id.EmailReg);
        passwordReg = (EditText) findViewById(R.id.PasswordReg);
        mAuth = FirebaseAuth.getInstance();
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
        //IMPLEMENT FIREBASE AUTHENTICATION HERE.
        

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterConfirm:
                Register();
            break;
            case R.id.LoginReg:
                startActivity(new Intent(this, BorrowedActivity.class));
                break;
        }
    }
}
