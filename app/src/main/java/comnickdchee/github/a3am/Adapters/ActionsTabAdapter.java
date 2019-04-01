package comnickdchee.github.a3am.Adapters;



import android.content.Context;

import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v7.widget.CardView;

import android.support.v7.widget.RecyclerView;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



import comnickdchee.github.a3am.Models.Book;

import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.R;

import comnickdchee.github.a3am.Activities.ViewBookActivity;


public class ActionsTabAdapter extends RecyclerView.Adapter<ActionsTabAdapter.ViewHolder> {



    private static final String TAG = " In_RecyclerViewAdapter";
    private ArrayList<Book> mBookList;
    private Context mContext;


    /** Constructor for this class
     * Takes in a context and the array list of all the books it needs to show*/
    public ActionsTabAdapter(Context mContext, ArrayList<Book> BookList) {
        this.mBookList = BookList;
        this.mContext = mContext;
    }



    /** Inflates the view to hold the cards
     * ViewGroup is the view on which the adapter will be located
     * i holds the index of that view*/
    @NonNull
    @Override
    public ActionsTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // Creates a view based on the actions_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actions_card, parent, false);
        ActionsTabAdapter.ViewHolder holder = new ActionsTabAdapter.ViewHolder(view);
        return holder;
    }

    /** This populates the cards with the data from the array list
     * holder refers to the View holder this recycler view is holding
     * i stores the index for the array list*/
    @Override
    public void onBindViewHolder(ActionsTabAdapter.ViewHolder holder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");

        // one file that contains a bunch of conditions for making a recycler view.
        loadImageFromBookID(holder.ivBook,mBookList.get(i).getBookID());
        holder.tvBookTitle.setText(mBookList.get(i).getTitle());
        holder.tvAuthorName.setText(mBookList.get(i).getAuthor());
        holder.tvISBN.setText(mBookList.get(i).getISBN());
        if (mBookList.get(i).getStatus() == Status.Accepted){
            holder.tvRequestCount.setVisibility(View.INVISIBLE);
            holder.tvRequestTag.setText("Request Accepted");
        } else {
            holder.tvRequestCount.setText(Integer.toString(mBookList.get(i).getRequests().size()));
            holder.tvRequestTag.setText("Total Requests: ");
        }


        // On click event when a card is clicked
        holder.actionsItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mBookList.get(i));
                if (mBookList.get(i).getTitle() == "PlaceHolder") {
                    Toast.makeText(mContext, mBookList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, ViewBookActivity.class);
                    intent.putExtra("ActionBook", mBookList.get(i));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /** Loads image of book with bookID = bookID and places it on the load imageView*/
    public void loadImageFromBookID(ImageView load, String bookID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(bookID);
        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String DownloadLink = uri.toString();
                Picasso.with(mContext).load(DownloadLink).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
            }
        });

    }

    /** Returns the size of the mBookList*/
    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    /** A view Holder to hold the views of each Individual Cards
     * This is the view holder that th recycler viewer uses*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Sets the variables to identify each items in the view

        public ImageView ivBook;
        public TextView tvBookTitle;
        public TextView tvAuthorName;
        public TextView tvRequestCount;
        public TextView tvRequestTag;
        public TextView tvISBN;
        public CardView actionsItemView;

        // The views are initialized here.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivBookPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            actionsItemView = itemView.findViewById(R.id.cvActions);
            tvAuthorName = itemView.findViewById(R.id.tvAuthor);
            tvRequestCount = itemView.findViewById(R.id.tvRequests);
            tvRequestTag = itemView.findViewById(R.id.tvRequestsTag);
            tvISBN = itemView.findViewById(R.id.tvISBN);

        }

    }
}