package comnickdchee.github.a3am;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.oob.SignUp;
import com.google.firebase.auth.FirebaseAuth;

public class BorrowedActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrowed);
        findViewById(R.id.RegisterBtn).setOnClickListener(this);
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

        }
    }
}
