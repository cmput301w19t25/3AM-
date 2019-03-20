package comnickdchee.github.a3am.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comnickdchee.github.a3am.R;

/**
 * @author Asma
 *  MessageFragment extends Fragment
 *  it overwrites onCreateView
 */
public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Opens a fragment which will show the messages
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}