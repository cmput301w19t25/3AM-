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
import comnickdchee.github.a3am.Barcode.BarcodeScanner;
import comnickdchee.github.a3am.R;

/**
 * View Book Activity - Activity that lets owners look at the
 * requests on a given book that they own. The user can accept
 * and decline requests.
 */
public class ViewRBookActivity extends AppCompatActivity {

    private static final int ISBN_READ = 42;
    private RecyclerView rvRequests;
    private static ArrayList<String> requesters;
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button receiveButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_r_books);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        backButton = findViewById(R.id.backIV);
        receiveButton = findViewById(R.id.receiveBookButton);

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRBookActivity.this, BarcodeScanner.class);
                startActivityForResult(intent,ISBN_READ);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

     /*   //button click for owner to specify pick up location
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ViewRBookActivity.this, MapsActivity.class);
                ViewRBookActivity.this.startActivity(myIntent);
            }
        });*/




        /*rvRequests = findViewById(R.id.rvViewBookRequests);
        layoutManager = new LinearLayoutManager(this);
        requesters = new ArrayList<String>();
        requesters.add("Zaheen Rahman");
        requesters.add("Ismaeel Bin Mohiuddin");
        requestersAdapter = new RequestersAdapter(this, requesters);
        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestersAdapter);*/

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY RESULT", "onActivityResult: CALLED");

        if (requestCode == ISBN_READ && resultCode == RESULT_OK && data != null){
            String isbn = data.getStringExtra("isbn");
            Log.d("ISBN Retrieved", isbn);

        }
    }
}
