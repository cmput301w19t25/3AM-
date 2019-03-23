package comnickdchee.github.a3am.Backend;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.User;

public interface UserListCallback {
    void onCallback(ArrayList<User> users);
}
