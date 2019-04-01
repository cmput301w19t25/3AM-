package comnickdchee.github.a3am.Backend;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.User;

/**
 * Interface callback class that retrieves a list of user data
 * when something in the database changes. These callbacks are
 * found in the getters methods of the backend class, and
 * are used to perform a sequence of actions when something
 * relevant happens.
 */
public interface UserListCallback {
    void onCallback(ArrayList<User> users);
}
