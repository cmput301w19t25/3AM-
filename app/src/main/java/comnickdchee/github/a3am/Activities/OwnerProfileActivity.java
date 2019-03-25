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
import android.widget.TextView;
import android.widget.Toast;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Fragments.HomeFragment;
import comnickdchee.github.a3am.Fragments.MessageFragment;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerProfileActivity extends AppCompatActivity {

    ImageView backButton;
    private static final int ISBN_READ = 42;
    private Book actionBook = new Book();
    private User owner = new User();
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
        String ownerID = actionBook.getOwnerID();
        backend.getUser(ownerID, new UserCallback() {
            @Override
            public void onCallback(User user) {
                owner = user;
                getPageData();
            }
        });

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
            backend.updateExchange(actionBook, ExchangeType.OwnerReceive);


        }
    }

    public void getPageData() {
        CircleImageView userPhoto = findViewById(R.id.UserImage);
        ImageView bookImage = findViewById(R.id.bookImage);
        TextView phone = findViewById(R.id.phoneTV);
        TextView email = findViewById(R.id.emailTV);
        TextView username = findViewById(R.id.usernameTV);
        TextView rating = findViewById(R.id.ratingTV);
        TextView bookTitle = findViewById(R.id.bookTitleTV);
        TextView bookAuthor = findViewById(R.id.authorNameTV);
        TextView bookISBN = findViewById(R.id.ISBNTv);

        Log.d(owner.getUserName(), "onCallback: Borrower");

        username.setText(owner.getUserName());
        phone.setText(owner.getPhoneNumber());
        email.setText(owner.getEmail());
        ///TODO: rating.setText(borrower.getRating());
        Log.d(actionBook.getISBN(), "getPageData: ");
        bookTitle.setText(actionBook.getTitle());
        bookAuthor.setText(actionBook.getAuthor());
        bookISBN.setText(actionBook.getISBN());

    }
}
