package comnickdchee.github.a3am;

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

import comnickdchee.github.a3am.Models.User;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Gets Current User
        mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://am-d5edb.appspot.com").child(userEmail+"/"+"dp"+ ".jpg");

        //Gets the textView fields
        TextView ProfileName = (TextView)view.findViewById(R.id.userNameFragmentProfile);
        final TextView Address = (TextView)view.findViewById(R.id.addressEditProfileFragment);
        final TextView PhoneNumber = (TextView)view.findViewById(R.id.phoneNumberEditProfileFragment);
        final TextView Email = (TextView)view.findViewById(R.id.emailEditProfileFragment);

        //Sets the name
        ProfileName.setText(mAuth.getCurrentUser().getDisplayName());
        DatabaseReference ref = database.getReference().child(mAuth.getUid());

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
                Picasso.with(getContext()).load(uri.toString()).fit().into(iv);
                //Handle whatever you're going to do with the URL here
            }
        });

        return view;
    }



}
