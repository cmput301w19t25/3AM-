package comnickdchee.github.a3am.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import comnickdchee.github.a3am.Models.Book;

public class RequestStatusGroup extends ExpandableGroup<Book> {
    public RequestStatusGroup(String title, List<Book> items) {
        super(title, items);
    }
}
