package comnickdchee.github.a3am.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import comnickdchee.github.a3am.Activities.SignInActivity;
import comnickdchee.github.a3am.R;

/**
 * @author
 * LogooutFragment extends Fragment
 * it overwrites onCreateView
 *
 */

public class LogoutFragment extends Fragment {
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(getContext(), "Bye " +mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        
        Intent i = new Intent();
        i.setClass(getActivity(), SignInActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0,0);
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}
