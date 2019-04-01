package comnickdchee.github.a3am;

import android.media.Image;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;


public class UserUnitTest {
    private ArrayList<User> testlist = new ArrayList<User>();
    private ArrayList<Book> booklist = new ArrayList<Book>();

    //Backend backend = Backend.getBackendInstance();
    User user1 = new User("Name","example@exm.com",
            "Dubai","0123-123-3456");

    @Before


    @Test
    public void test_rejectRequest() {
        User user2 = new User("Gorib","mod@exm.com",
                "Dhaka, Bangladesh", "0124-123-3456");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.rejectRequest(book, user2);
    }

    @Test
    public void test_addRequestedBook() {
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.addRequestedBook(request);
        ArrayList<String> books = user1.getRequestedBooks();
        booklist.add(request);
        assertTrue(booklist.contains(request));
    }

    @Test
    public void test_returnBook() {
        User user2 = new User("Gorib","mod@exm.com",
                "Dhaka, Bangladesh", "0124-123-3456");
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user2);
        String requestID = request.getBookID();
        Exchange exchange = new Exchange(user2, user1, "test location", false, request);
        //user1.returnBook(requestID);

        assertEquals(Status.Available, request.getStatus());
    }


    @Test
    public void test_setImage() {
        Image image = null;
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.setImage(book, image);
        assertEquals(book.getImage(), image);
    }

    @Test
    public void test_removeImage() {
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        assertEquals(book.getImage(),null);
    }

    @Test
    public void test_removeOwnedBook() {
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        Book book2 = new Book("12345678","Harius Potterus","J.K. Rolling",user1);

        user1.removeOwnedBook(book);
        booklist.add(book);
        booklist.remove(book2);
        assertTrue(!booklist.contains(book2));
        }



    @Test
    public void test_editBook() {
        User user = new User ("username1",
                "email.yahoo.com", "2132-52Ave","780-111-2222");
        testlist.add(user);
        User user2 = new User("userName2",
                "email@gmailcom", "1111-25Ave", "780-111-4444");
        testlist.add(user2);

        User user3 = new User("userName3",
                "email@hotmail.com",  "1111-22Street","780-111-6666");
        testlist.add(user3);

        Book book1 = new Book("9876543211234", "harry Potter", "j k rowling");
        booklist.add(book1);

        book1.setCurrentBorrowerID("user2");
        user.addOwnedBook(book1);               //add book to the list of owned book

        Book book2 = new Book("9876543211234","Harry Potter", "J K Rowlings");
        booklist.set(0,book2);
        assertTrue(booklist.contains(book2));
        booklist.clear();

    }


    @Test
    public void test_editProfile() {
        User user = new User("username1",
                "email.yahoo.com", "2132-52Ave",  "780-111-2222");
        testlist.add(user);
        //edit contact info
        User newuser = new User("user_name","email@gmailcom","2132-52Ave","780-222-4444");
        testlist.set(0,newuser);
        assertTrue(testlist.contains(newuser));

        //check if contact info was edited successfull
    }
}