package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comnickdchee.github.a3am.Adapters.ProductAdapter;
import comnickdchee.github.a3am.Company;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.Product;
import comnickdchee.github.a3am.R;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private ArrayList<Book> data = new ArrayList<>();

    public RequestsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrowed, container, false);

        Bundle args = getArguments();
        ArrayList<Book> AcceptedRequests = (ArrayList<Book>) args.getSerializable("data");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Company> companies = new ArrayList<>();

        ArrayList<Book> googleProduct = new ArrayList<>();

        User user1 = new User("User Name","sample@sc.ca","12345","98708");

        googleProduct.add(new Book("Google AdSense","user1@gmail.com","XXXXX",user1));
        googleProduct.add(new Book("Google Nonsense","user2@gmail.com","XXXXX",user1));
        googleProduct.add(new Book("Google BalSense","user3@gmail.com","XXXXX",user1));

        Company google = new Company("Google", googleProduct);
        companies.add(google);

        ArrayList<Book> microsoftProduct = new ArrayList<>();
        microsoftProduct.add(new Book("Google ShitSense","user4@gmail.com","XXXXX",user1));
        microsoftProduct.add(new Book("Google DickSense","user5@gmail.com","XXXXX",user1));

        Company microsoft = new Company("Microsoft", microsoftProduct);
        companies.add(microsoft);

        ProductAdapter adapter = new ProductAdapter(companies);
        recyclerView.setAdapter(adapter);

        return view;

    }

}
