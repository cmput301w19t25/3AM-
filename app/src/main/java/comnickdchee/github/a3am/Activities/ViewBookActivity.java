package comnickdchee.github.a3am.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RequestersAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserListCallback;
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

/**
 * View Book Activity - Activity that lets owners look at the
 * requests on a given book that they own. The user can accept
 * and decline requests.
 */
public class ViewBookActivity extends AppCompatActivity {

    private static final int ISBN_READ = 42;
    private RecyclerView rvRequests;
    private ArrayList<User> requesters = new ArrayList<>();
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ownerHandoverButton;
    private Button mapsButton;
    private Backend backend = Backend.getBackendInstance();
    private Book actionBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        //button click for owner to specify pick up location
//        mapsButton = (Button) findViewById(R.id.mapsButton);
//        mapsButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(ViewBookActivity.this, MapsActivity.class);
//                ViewBookActivity.this.startActivity(myIntent);
//                }
//        });


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // Get the contents of the intent
        Intent intent = getIntent();
        actionBook = intent.getExtras().getParcelable("ActionBook");

        rvRequests = findViewById(R.id.rvViewBookRequests);
        ownerHandoverButton = findViewById(R.id.bOwnerHandover);
        layoutManager = new LinearLayoutManager(this);
        requestersAdapter = new RequestersAdapter(this, requesters, actionBook);
        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestersAdapter);


        backend.getRequesters(actionBook, new UserListCallback() {
            @Override
            public void onCallback(ArrayList<User> users) {
                requesters.clear();
                requesters.addAll(users);
                requestersAdapter.notifyDataSetChanged();
            }
        });

        ownerHandoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set exchange type
                Intent intent = new Intent(ViewBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
                //
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);
            String bookISBN = actionBook.getISBN();

            if (isbn.equals(bookISBN)) {
                backend.updateExchange(actionBook, ExchangeType.BorrowerReceive);
            } else {
                Toast.makeText(this, "ISBN Not Matched with book", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
