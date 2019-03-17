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

        ArrayList<Product> googleProduct = new ArrayList<>();
        googleProduct.add(new Product("Google AdSense"));
        googleProduct.add(new Product("Google Drive"));
        googleProduct.add(new Product("Google Mail"));
        googleProduct.add(new Product("Google Doc"));
        googleProduct.add(new Product("Android"));

        Company google = new Company("Google", googleProduct);
        companies.add(google);

        ArrayList<Product> microsoftProduct = new ArrayList<>();
        microsoftProduct.add(new Product("Windows"));
        microsoftProduct.add(new Product("SkyDrive"));
        microsoftProduct.add(new Product("Microsoft Store"));
        microsoftProduct.add(new Product("Microsoft Office"));

        Company microsoft = new Company("Microsoft", microsoftProduct);
        companies.add(microsoft);

        ProductAdapter adapter = new ProductAdapter(companies);
        recyclerView.setAdapter(adapter);

        return view;

    }

}
