package comnickdchee.github.a3am;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;

import static org.junit.Assert.assertEquals;


public class BookUnitTests {
    private List<Book> BookTest = new ArrayList<Book>();
    User user1 = new User("Name","example@exm.com",
            "Dubai","0123-123-3456");
    private String id;

    @Test
    public void test_setOwner() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        b.setOwner(user1);
        assertEquals(user1,b.getOwner());
    }

    @Test
    public void test_setCurrentBorrower() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        id = user1.getUserID();
        String borrowerid = b.getCurrentBorrowerID();
        assertEquals(id, borrowerid);
    }


    @Test
    public void test_getISBN() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        assertEquals(b.getISBN().length(), 13);
    }

    @Test
    public void test_setISBN() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        b.setISBN("9876543214321");
        assertEquals("9876543214321", b.getISBN());
    }

    @Test
    public void test_getAuthor() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        assertEquals(b.getAuthor(), "J R R martin");
    }
    @Test
    public void test_getTitle() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        assertEquals(b.getTitle(), "HarryPotter");
    }
    @Test
    public void setAuthorName() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        b.setAuthor("J K Rowlin");
        assertEquals("J K Rowlin", b.getAuthor());
    }
    @Test
    public void test_setTitle() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        b.setTitle("Harry");
        assertEquals("Harry", b.getTitle());
    }

    @Test
    public void test_getStatus() {
        Status testStatus = Status.Available;
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        assertEquals(testStatus, b.getStatus());
    }

    @Test
    public void test_setStatus() {
        Status testStatus = Status.Accepted;
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        b.setStatus(testStatus);
        assertEquals(testStatus, b.getStatus());
    }



}
