package comnickdchee.github.a3am;

import android.media.Image;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import comnickdchee.github.a3am.models.Book;
import comnickdchee.github.a3am.models.Exchange;
import comnickdchee.github.a3am.models.User;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {
    User user1 = new User("Name","0123-123-3456","example@exm.com",
            "Walter White","Dubai");

    @Test
    public void test_acceptRequests()
    {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.acceptRequest(book,user2);
        ArrayList<Exchange> exchangeList = user1.getExchangeList();
        Exchange exchange1 = exchangeList.get(exchangeList.size() - 1);
        Exchange exchange2 = new Exchange(user1,user2,"",false,book);
        assertEquals(exchange1, exchange2);
    }

    public void test_rejectRequest()
    {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);


        user1.rejectRequest(book, user2);


    }

    public void test_addOwnedBooks(){

        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.addOwnedBook(book);
        ArrayList<Book> books = user1.getOwnedBookList();
        Book newBook = books.get(books.size() - 1);
        assertEquals(newBook,book);

    }

    public void test_addRequestedBook() {
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.addRequestedBook(request);
        ArrayList<Book> books = user1.getRequestedBooksList();
        Book newRequest = books.get(books.size() - 1);
        assertEquals(newRequest,request);
    }

    public void test_returnBook() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user2);
        user1.returnBook(request);

        user2.getExchangeList().get(user2.getExchangeList().size() - 1);
        //????????????????????????????
    }

    public void test_setLocation() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        Exchange exchange = new Exchange(user1,user2,"",false,book);
        user1.setLocation(exchange, "Dhaka");
        assertEquals(user1.getLocation(exchange), "Dhaka");
    }
    public void test_setImage() {
        Image image = null;
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.setImage(book, image);
        assertEquals(book.getImage(), image);
    }
    public void test_removeImage() {
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        assertEquals(book.getImage(),null);
    }
    public void test_removeOwnedBook() {
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.removeOwnedBook(book);
        ArrayList<Book> books = user1.getOwnedBookList();
        Boolean x = false;
        if (books.contains(book)) {
            x = true;
        }
        assertTrue(x);
    }

}