package comnickdchee.github.a3am.Backend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comnickdchee.github.a3am.Models.User;

/**
 * Backend class that handles all the logic
 * required to access data and retrieve data from the Firebase
 * database.
 */
public class Backend {
    // Instance of the singleton
    private static Backend instance = new Backend();

    // Get the instance of our database
    private static final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // References to static objects that we need over the lifetime of our singleton
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Have a direct reference to the user operating the app
    private static User mCurrentUser = null;

    // TODO: User should store an instance of his/her firebase user reference.
    private static final FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

    /** Empty constructor for singleton. Prevents direct instantiate outside of class. */
    private Backend() {}

    /** Get the actual instance of the backend class. */
    public static Backend getBackendInstance() {
        return instance;
    }

    /** Returns the current user of the model class. */
    public User getCurrentUser() {
        if (mCurrentUser == null) {
            loadCurrentUserData();
        }

        return mCurrentUser;
    }

    /**
     * Class that loads all the current user into the user class referenced
     * by the singleton. Note that this is an asynchronous load, so we change the information
     * everytime the information in Firebase changes.
     */
    public void loadCurrentUserData() {
        if (mFirebaseUser != null) {
            // Get the actual contents of the user class
            String uid = mFirebaseUser.getUid();
            DatabaseReference userRef = mFirebaseDatabase.getReference("users").child(uid);

            // Whenever the user reference is loaded, load the data of the user
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mCurrentUser = dataSnapshot.getValue(User.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        // If we got to the main screen without logging in
        } else {
            Log.e("Sign-in error!", "Could not get current user from database!");
            System.exit(1);
        }
    }
}
