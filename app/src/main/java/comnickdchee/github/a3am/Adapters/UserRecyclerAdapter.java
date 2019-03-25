package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {


    private static final String TAG = "In_RecyclerViewAdapter";

    private ArrayList<Exchange> mExchanges;
    private String type;
    private Context mContext;

    public UserRecyclerAdapter( Context mContext, String type, ArrayList<Exchange> ExchangeList) {
        this.mExchanges = ExchangeList;
        this.type = type;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // Creates a view based on the mybooks_card.xml

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooks_card,parent,false);
        UserRecyclerAdapter.ViewHolder holder  = new UserRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserRecyclerAdapter.ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

//        // one file that contains a bunch of conditions for making a recycler view.
//        holder.tvBookTitle.setText(mExchanges.get(i).getBook().getTitle());
//        holder.tvAuthorName.setText(mExchanges.get(i).getBook().getAuthor());
//        holder.tvISBN.setText(mExchanges.get(i).getBook().getISBN());
//        if (type == "Owned") {
//            holder.tvUser.setText(mExchanges.get(i).getBorrower().getUserName());
//            holder.tvUserRole.setText("Borrower: ");
//        }
//        else {
//            holder.tvUser.setText(mExchanges.get(i).getOwner().getUserName());
//            holder.tvUserRole.setText("Owner: ");
//        }
//
//        // On click event when a card is clicked
//        holder.actionsItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + mExchanges.get(i).getBook().getTitle());
//                Toast.makeText(mContext, mExchanges.get(i).getBook().getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mExchanges.size();
    }


    // A view Holder to hold the views of each Individual Cards

    public static class ViewHolder extends RecyclerView.ViewHolder{

        // views inside ViewHolder
        public ImageView ivBook;
        public TextView tvBookTitle;
        public TextView tvAuthorName;
        public TextView tvISBN;
        public TextView tvUser;
        public TextView tvUserRole;
        public CardView actionsItemView;

        // The Data inside the View Holder are set here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set the views
            ivBook = itemView.findViewById(R.id.ivBookPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            actionsItemView = itemView.findViewById(R.id.cvUserInfo);
            tvAuthorName = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);

        }
    }
}
