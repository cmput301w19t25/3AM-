package comnickdchee.github.a3am.Adapters;



import android.content.Context;

import android.content.Intent;

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


import java.util.ArrayList;



import comnickdchee.github.a3am.Models.Book;

import comnickdchee.github.a3am.R;

import comnickdchee.github.a3am.Activities.ViewBookActivity;


public class ActionsTabAdapter extends RecyclerView.Adapter<ActionsTabAdapter.ViewHolder> {



    private static final String TAG = "In_RecyclerViewAdapter";
    private ArrayList<Book> mBookList;
    private Context mContext;

    public ActionsTabAdapter(Context mContext, ArrayList<Book> BookList) {
        this.mBookList = BookList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public ActionsTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // Creates a view based on the actions_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actions_card, parent, false);
        ActionsTabAdapter.ViewHolder holder = new ActionsTabAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActionsTabAdapter.ViewHolder holder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");

        // one file that contains a bunch of conditions for making a recycler view.
        holder.tvBookTitle.setText(mBookList.get(i).getTitle());
        holder.tvAuthorName.setText(mBookList.get(i).getAuthor());
        holder.tvISBN.setText(mBookList.get(i).getISBN());

        // On click event when a card is clicked
        holder.actionsItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mBookList.get(i));
                if (mBookList.get(i).getTitle() == "PlaceHolder") {
                    Toast.makeText(mContext, mBookList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, ViewBookActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override

    public int getItemCount() {
        return mBookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Sets the variables to identify each items in the view

        public ImageView ivBook;
        public TextView tvBookTitle;
        public TextView tvAuthorName;
        public TextView tvISBN;
        public CardView actionsItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivBookPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            actionsItemView = itemView.findViewById(R.id.cvActions);
            tvAuthorName = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);

        }

    }
}