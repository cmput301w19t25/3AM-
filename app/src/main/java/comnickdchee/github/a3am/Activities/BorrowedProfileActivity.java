package comnickdchee.github.a3am.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
import comnickdchee.github.a3am.R;

public class BorrowedProfileActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_profile);

        button1 = (Button) findViewById(R.id.message);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //View v = inflater.inflate(R.layout.fragment_borrowed, container, false);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      new MessageFragment()).commit();
            }
        });

        button2 = findViewById(R.id.returned);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(mContext, ViewBookActivity.class);
                //mContext.startActivity(intent);
            }
        });
    }
}
