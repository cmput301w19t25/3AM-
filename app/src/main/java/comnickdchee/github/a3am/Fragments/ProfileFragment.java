package comnickdchee.github.a3am.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import comnickdchee.github.a3am.Activities.EditProfile;
import comnickdchee.github.a3am.Models.User;
import comnickdchee.github.a3am.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @auhtor Tatenda
 * ProfileFragment extends Fragment
 * it overwrites onCreateView
 */

public class ProfileFragment extends Fragment {

    private FloatingActionButton edit;
    private String DownloadLink;
    private FirebaseAuth mAuth;
    private FloatingActionButton editFAB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Ref","Image gotten");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Gets Current User
        mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child("users").child(mAuth.getUid()+".jpg");
        Log.d("Ref","Image gotten");
        //Gets the textView fields
        TextView ProfileName = (TextView)view.findViewById(R.id.userNameFragmentProfile);
        final TextView Address = (TextView)view.findViewById(R.id.addressEditProfileFragment);
        final TextView PhoneNumber = (TextView)view.findViewById(R.id.phoneNumberEditProfileFragment);
        final TextView Email = (TextView)view.findViewById(R.id.emailEditProfileFragment);
        editFAB = view.findViewById(R.id.editFAB);

        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editActivity = new Intent(getActivity(), EditProfile.class);
                editActivity.putExtra("userID", mAuth.getCurrentUser().getUid());
                editActivity.putExtra("username", ProfileName.getText());
                editActivity.putExtra("email", Email.getText());
                editActivity.putExtra("address", Address.getText());
                editActivity.putExtra("phone", PhoneNumber.getText());
                getActivity().startActivity(editActivity);
            }
        });

        //Sets the name
        ProfileName.setText(mAuth.getCurrentUser().getDisplayName());

        DatabaseReference ref = database.getReference().child("users").child(mAuth.getUid());
        Log.d("Ref", "onCreateView: "+ ref.toString());
        //Sets the other details from the reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("address").getValue().toString();
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();

                Email.setText(email);
                Address.setText(address);
                PhoneNumber.setText(phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Gets the user's profile picture
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                DownloadLink = uri.toString();
                CircleImageView iv = (CircleImageView) view.findViewById(R.id.profilePictureEditFragment);
                Picasso.with(getContext()).load(uri.toString()).placeholder(R.drawable.ccc).error(R.drawable.ccc).fit().into(iv);
                //Handle whatever you're going to do with the URL here
            }
        });

        return view;
    }



}
