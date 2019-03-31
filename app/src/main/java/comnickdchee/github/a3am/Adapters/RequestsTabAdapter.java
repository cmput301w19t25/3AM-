package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import comnickdchee.github.a3am.Activities.ViewRBookActivity;

import comnickdchee.github.a3am.Backend.Backend;
import comnickdchee.github.a3am.Backend.UserCallback;
import comnickdchee.github.a3am.Models.RequestStatusGroup;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/** This is an Expandable Recycler Adapter
 *  The RequestGroupViewHolder determines the view of the Groups. i.e. "Accepted" and "Pending"
 *  The RequestsViewHolder determines the view of the Requests in each of these groups*/

public class RequestsTabAdapter extends ExpandableRecyclerViewAdapter<RequestsTabAdapter.RequestGroupViewHolder, RequestsTabAdapter.RequestsViewHolder> {

    private static final String TAG = "In_MyRequestsAdapter";
    private Context mContext;
    private Backend backend = Backend.getBackendInstance();

    /** It takes an ArrayList of RequestGroupViewHolder as the argument */
    public RequestsTabAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.mContext = context;

    }

    /** This function creates the view for the parent that will be inflated in the recycler view.*/
    @Override
    public RequestGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        // This creates the RequestGroupViewHolder view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_company, parent, false);
        return new RequestGroupViewHolder(v);
    }

    /** This function creates the view for the child that will be inflated in the recycler view.*/
    @Override
    public RequestsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        // This creates the RequestViewHolder view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new RequestsViewHolder(v);
    }

    /** This function will bind the values to the child of the expandable recycler view*/
    @Override
    public void onBindChildViewHolder(RequestsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        // This Binds the child items to the holder
        final Book book = (Book) group.getItems().get(childIndex);
        holder.bind(book);
        loadImageFromOwnerID(holder.ownerIV,book.getOwnerID());

        // Here the on click action is created for the cards. This opens the ViewRBookActivity
        holder.cvUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + ((Book) group.getItems().get(childIndex)).getTitle());
                Toast.makeText(mContext, ((Book) group.getItems().get(childIndex)).getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, ViewRBookActivity.class);
                Book book = ((Book) group.getItems().get(childIndex));
                intent.putExtra("acceptedBook",book);
                mContext.startActivity(intent);

            }
        });

    }


    /** This function loads the image of an user with userID:"uID" to the "load" imageView specified*/
    public void loadImageFromOwnerID(ImageView load, String uID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(uID+".jpg");

        Log.e("Tuts+", storageRef.toString());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                String imgUrl = uri.toString();

                Picasso.with(mContext).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(load);
            }
        });

    }

    /** This is the view holder that holds the parent item of the recycle view*/
    @Override
    public void onBindGroupViewHolder(RequestGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        // This Binds the group items to the holder
        final RequestStatusGroup requestStatusGroup = (RequestStatusGroup) group;
        holder.bind(requestStatusGroup);
    }

    /** Here is the class for the RequestGroupViewHolder that this adapter uses */
    public static class RequestGroupViewHolder extends GroupViewHolder {

        private TextView mTextView;
        private ImageView arrow;

        /** This is the constructor for the View Holder class for the group */
        public RequestGroupViewHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.textView);
            arrow = itemView.findViewById(R.id.arrow);
        }

        /** This is where the heading is bound to the group's headers */
        public void bind(RequestStatusGroup requestStatusGroup) {
            mTextView.setText(requestStatusGroup.getTitle());
        }

        // The codes below are for animation when the groups are expanded/collapsed

        /** This makes the expand animation*/
        @Override
        public void expand() {
            animateExpand();
        }

        /** This makes the collapse animation*/
        @Override
        public void collapse() {
            animateCollapse();
        }

        /** This makes the expand animation for the arrow*/
        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        /** This makes the collapse animation for the arrow*/
        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }


    /** Here is the class for the RequestsViewHolder that this adapter uses */
    public static class RequestsViewHolder extends ChildViewHolder {
        private CircleImageView ownerIV;
        private TextView tvBookTitle;
        private TextView tvISBN;
        private TextView tvAuthor;
        private TextView tvUserRole;
        private TextView tvOwner;
        private CardView cvUserInfo;
        private Backend backend = Backend.getBackendInstance();

        /** Here the views are initialized*/
        public RequestsViewHolder(View itemView) {
            super(itemView);
            ownerIV = itemView.findViewById(R.id.ivUserPhoto);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvOwner = itemView.findViewById(R.id.tvUser);
            cvUserInfo = itemView.findViewById(R.id.cvUserInfo);
        }

        /** Here the views are bound to the right values */
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

