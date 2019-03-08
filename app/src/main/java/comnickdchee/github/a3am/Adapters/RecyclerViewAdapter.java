package comnickdchee.github.a3am.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "In_RecyclerViewAdapter";

    private ArrayList<String> mUsernames;
    private Context mContext;

    public RecyclerViewAdapter( Context mContext, ArrayList<String> mUsernames) {
        this.mUsernames = mUsernames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.username.setText(mUsernames.get(i));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mUsernames.get(i));
                Toast.makeText(mContext, mUsernames.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsernames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageIcon;
        TextView username;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.imageIcon);
            username = itemView.findViewById(R.id.username);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }


}
