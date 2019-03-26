package comnickdchee.github.a3am.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import comnickdchee.github.a3am.Activities.HomepageActivity;
import comnickdchee.github.a3am.Activities.ViewOwnedBook;
import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {


    private static final String TAG = "In_RecyclerViewAdapter";
    FirebaseStorage storage;
    String DownloadLink;
    private ArrayList<Book> mBooks = new ArrayList<>();
    private Context mContext;
    //private ImageButton option;                         // options button for Edit/Delete
    private int currentPos;
    private Backend backend = Backend.getBackendInstance();

    public BookRecyclerAdapter( Context mContext, ArrayList<Book> BookList) {
        this.mBooks = BookList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public BookRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // Creates a view based on the mybooks_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooks_card,parent,false);
        BookRecyclerAdapter.ViewHolder holder  = new BookRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookRecyclerAdapter.ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        // one file that contains a bunch of conditions for making a recycler view.

        holder.tvBookTitle.setText(mBooks.get(i).getTitle());
        holder.tvAuthorName.setText(mBooks.get(i).getAuthor());
        holder.tvISBN.setText(mBooks.get(i).getISBN());
        holder.tvStatus.setText(mBooks.get(i).getStatus().name());

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("BookImages").child(mBooks.get(i).getImage());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                Picasso.with(mContext).load(DownloadLink).placeholder(R.drawable.ccc).error(R.drawable.ccc).into(holder.ivBook);
            }
        });
        // On click event when a card is clicked
        holder.actionsItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d(TAG, "onClick: clicked on: " + mBooks.get(i));
                Intent intent = new Intent(mContext, ViewOwnedBook.class);
                String bookID = mBooks.get(i).getBookID();
                Log.d(bookID, "keyFromRecycler: ");
                intent.putExtra("key", bookID);
                mContext.startActivity(intent);
            }
        });

        /**
         * Handles on clicks for the options button on the top right corner of the
         * ViewHolder. Here, we create a new PopupMenu object and inflate it using
         * the menu_book menu.
         */
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);

                // inflate PopupMenu from XML
                popup.inflate(R.menu.book_menu);

                // adding listeners for each item in the popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemViewEdit:
                                clickEdit();
                                return true;
                            case R.id.itemDelete:
                                clickDelete();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // show the popup afterwards
                popup.show();

            }

            /**
             * Called when Edit is clicked on the PopupMenu. This creates an intent
             * to the "Edit Books Activity" ViewOwnedBooks
             */
            void clickEdit() {
                // create intents here and start new activity

             Intent intent = new Intent(mContext, ViewOwnedBook.class);
             mContext.startActivity(intent);
            }


            /**
             * Called when the user clicks on the Delete option in the PopupMenu object.
             */
            void clickDelete() {
                Log.d(mBooks.get(i).getBookID(), "FROM ADAPTER: ");
               backend.deleteBook(mBooks.get(i));
               notifyDataSetChanged();
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
        private ImageButton option;                         // options button for Edit/Delete

        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivRequesterPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            tvAuthorName = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvBorrowedBy = itemView.findViewById(R.id.tvBorrowedBy);
            actionsItemView = itemView.findViewById(R.id.cvActions);
            option = (ImageButton) itemView.findViewById(R.id.ibOption);

//            imageIcon = itemView.findViewById(R.id.imageIcon);
//            username = itemView.findViewById(R.id.username);
//            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
