package comnickdchee.github.a3am;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.RecyclerViewAdapter;

public class ViewBookActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private static ArrayList<String> requesters;
    private RecyclerViewAdapter requestersAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        rvRequests = findViewById(R.id.rvViewBookRequests);
        layoutManager = new LinearLayoutManager(this);
        requesters = new ArrayList<String>();
        requestersAdapter = new RecyclerViewAdapter(this, requesters);
        rvRequests.setLayoutManager(layoutManager);
        rvRequests.setAdapter(requestersAdapter);

    }
}
