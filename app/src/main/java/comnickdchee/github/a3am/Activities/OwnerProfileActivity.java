package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;

public class OwnerProfileActivity extends AppCompatActivity {

    ImageView backButton;
    private static final int ISBN_READ = 42;
    private Book actionBook;
    Button transactionButton;
    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        backButton = findViewById(R.id.backIV);
        transactionButton = findViewById(R.id.transactionButton);
        transactionButton.setText("Return Book");

        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("passedBook");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerProfileActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });



        //getSupportActionBar().setTitle("Owner's Profile");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //////////
        //  button1 = (Button) findViewById(R.id.button3);
      //  button1.setOnClickListener(new View.OnClickListener() {

         /*   @Override
            public void onClick(View v) {

                Intent myIntent =  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
            }
        });*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);
            String bookISBN = actionBook.getISBN();

            if (isbn.equals(bookISBN)) {
                backend.updateExchange(actionBook, ExchangeType.OwnerReceive);
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
