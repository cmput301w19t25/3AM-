package comnickdchee.github.a3am.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RequestersAdapter;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserListCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;

/**
 * View Book Activity - Activity that lets owners look at the
 * requests on a given book that they own. The user can accept
 * and decline requests.
 */
public class ViewBookActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private ArrayList<User> requesters = new ArrayList<>();
    private RequestersAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ownerHandoverButton;

    private Backend backend = Backend.getBackendInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // Get the contents of the intent
        Intent intent = getIntent();
        Book actionBook = intent.getExtras().getParcelable("ActionBook");

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
                backend.updateExchange(actionBook, ExchangeType.BorrowerReceive);
            }
        });

    }
}
