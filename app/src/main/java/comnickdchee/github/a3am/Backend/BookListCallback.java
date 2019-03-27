package comnickdchee.github.a3am.Backend;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.Book;

public interface BookListCallback {
    void onCallback(ArrayList<Book> books);
}
