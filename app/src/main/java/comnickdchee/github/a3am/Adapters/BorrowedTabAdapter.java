package comnickdchee.github.a3am.Adapters;

import android.app.Activity;
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
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.ArrayList;

import comnickdchee.github.a3am.Activities.OwnerProfileActivity;
import comnickdchee.github.a3am.Activities.ViewBookActivity;
import comnickdchee.github.a3am.Activities.ViewRBookActivity;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

// This is the Recycler Adapter for the Borrowed tab
// The ViewHolder determines the view of the Requests in each of these groups

public class BorrowedTabAdapter extends RecyclerView.Adapter<BorrowedTabAdapter.ViewHolder> {

    private static final String TAG = "In_BorrowedTabAdapter";
    private ArrayList<Book> mBookList;
    private Context mContext;


    /** Constructor for this class
     * Takes in a context and the array list of all the books it needs to show*/
    public BorrowedTabAdapter(Context mContext, ArrayList<Book> BookList) {
        this.mBookList = BookList;
        this.mContext = mContext;
    }


    /** Inflates the view to hold the cards
     * ViewGroup is the view on which the adapter will be located
     * i holds the index of that view*/
    @NonNull
    @Override
    public BorrowedTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // Creates a view based on the user_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        BorrowedTabAdapter.ViewHolder holder = new BorrowedTabAdapter.ViewHolder(view);
        return holder;
    }

    /** This populates the cards with the data from the array list
     * holder refers to the View holder this recycler view is holding
     * i stores the index for the array list*/
    @Override
    public void onBindViewHolder(BorrowedTabAdapter.ViewHolder holder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");

        // Puts all the data based on the bind function in the Viewholder
        holder.bind(mBookList.get(i));
        loadImageFromOwnerID(holder.ownerIV, mBookList.get(i).getOwnerID());


        // On click event when a card is clicked
        holder.cvUserInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mBookList.get(i));
                Toast.makeText(mContext, mBookList.get(i).getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, OwnerProfileActivity.class);
                Book book = mBookList.get(i);
                intent.putExtra("passedBook",book);
                mContext.startActivity(intent);
            }
        });
    }

    /** Loads image of user with userID of uID and places it on the load imageView*/
    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(mContext).load(imgUrl).placeholder(R.drawable.ic_launcher3slanted).error(R.drawable.ic_launcher3slanted).into(load);
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
        private Backend backend = Backend.getBackendInstance();
        private CircleImageView ownerIV;
        private TextView tvBookTitle;
        private TextView tvISBN;
        private TextView tvAuthor;
        private TextView tvUserRole;
        private TextView tvOwner;
        private CardView cvUserInfo;

        // The views are initialized here.
        public ViewHolder(View itemView) {
            super(itemView);
            ownerIV = itemView.findViewById(R.id.ivUserPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvOwner = itemView.findViewById(R.id.tvUser);
            cvUserInfo = itemView.findViewById(R.id.cvUserInfo);
        }

        // The Data inside the View Holder are set here
        public void bind(Book book) {
            tvBookTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvISBN.setText(book.getISBN());
            tvUserRole.setText("Owner: ");
            backend.getUser(book.getOwnerID(), new UserCallback() {
                @Override
                public void onCallback(User user) {
                    tvOwner.setText(user.getUserName());
                }
            });

        }
    }
}
