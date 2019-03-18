package comnickdchee.github.a3am.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import comnickdchee.github.a3am.Models.RequestStatusGroup;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MyRequestsAdapter extends ExpandableRecyclerViewAdapter<MyRequestsAdapter.CompanyViewHolder, MyRequestsAdapter.ProductViewHolder> {

    public MyRequestsAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CompanyViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Book book = (Book) group.getItems().get(childIndex);
        holder.bind(book);
    }

    @Override
    public void onBindGroupViewHolder(CompanyViewHolder holder, int flatPosition, ExpandableGroup group) {
        final RequestStatusGroup requestStatusGroup = (RequestStatusGroup) group;
        holder.bind(requestStatusGroup);
    }

    public static class CompanyViewHolder extends GroupViewHolder {
        private TextView mTextView;
        private ImageView arrow;

        public CompanyViewHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.textView);
            arrow = itemView.findViewById(R.id.arrow);
        }

        public void bind(RequestStatusGroup requestStatusGroup) {
            mTextView.setText(requestStatusGroup.getTitle());
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    public static class ProductViewHolder extends ChildViewHolder {
        private TextView tvBookTitle;
        private TextView tvISBN;
        private TextView tvAuthor;
        private TextView tvUserRole;
        private TextView tvOwner;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvCardBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvOwner = itemView.findViewById(R.id.tvUser);
        }

        public void bind(Book book) {
            tvBookTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvISBN.setText(book.getISBN());
            tvUserRole.setText("Owner: ");
            tvOwner.setText(book.getOwner().getUserName());

        }
    }

}

