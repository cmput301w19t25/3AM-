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

import comnickdchee.github.a3am.Activities.RequestBookActivity;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private static final String TAG = "In_RecyclerViewAdapter";

    private ArrayList<Book> mBooks;
    private Context mContext;

    public SearchRecyclerAdapter( Context mContext, ArrayList<Book> BookList) {
        this.mBooks = BookList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // Creates a view based on the mybooks_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooks_card,parent,false);
        SearchRecyclerAdapter.ViewHolder holder  = new SearchRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchRecyclerAdapter.ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        // one file that contains a bunch of conditions for making a recycler view.
        holder.tvBookTitle.setText(mBooks.get(i).getTitle());
        holder.tvAuthorName.setText(mBooks.get(i).getAuthor());
        holder.tvISBN.setText(mBooks.get(i).getISBN());
        holder.tvStatus.setText(mBooks.get(i).getStatus().name());
//        if (mBooks.get(i).getCurrentBorrower() != null){
//            holder.tvBorrowedBy.setText("Borrowed By: " + mBooks.get(i).getCurrentBorrower());
//        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        if(mBooks.get(i).getBookID() != null) {
            StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(mBooks.get(i).getBookID());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e("Tuts+", "uri: " + uri.toString());
                    String DownloadLink = uri.toString();
                    Picasso.with(mContext).load(DownloadLink).placeholder(R.drawable.ccc).error(R.drawable.ccc).into(holder.ivBook);
                }
            });
        }

        // On click event when a card is clicked
        holder.actionsItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Searched " + mBooks.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), RequestBookActivity.class);
                int currentPos = holder.getAdapterPosition();
                intent.putExtra("SearchBook", mBooks.get(currentPos));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }


    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //        CircleImageView imageIcon;
//        TextView username;
//        RelativeLayout parentLayout;
        public ImageView ivBook;
        public TextView tvBookTitle;
        public TextView tvAuthorName;
        public TextView tvISBN;
        public TextView tvStatus;
        public TextView tvBorrowedBy;
        public CardView actionsItemView;

        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivRequesterPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            actionsItemView = itemView.findViewById(R.id.cvActions);
            tvAuthorName = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvBorrowedBy = itemView.findViewById(R.id.tvBorrowedBy);

//            imageIcon = itemView.findViewById(R.id.imageIcon);
//            username = itemView.findViewById(R.id.username);
//            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }

}
