package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
import comnickdchee.github.a3am.R;

public class BorrowedProfileActivity extends AppCompatActivity {

    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_profile);

        button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //View v = inflater.inflate(R.layout.fragment_borrowed, container, false);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      new MessageFragment()).commit();
            }
        });
    }
}