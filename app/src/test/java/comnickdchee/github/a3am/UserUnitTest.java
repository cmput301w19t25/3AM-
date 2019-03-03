package comnickdchee.github.a3am;

import android.media.Image;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;


public class UserUnitTest {
    User user1 = new User("Name","0123-123-3456","example@exm.com",
            "Walter White","Dubai");

    @Test
    public void test_acceptRequests() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.acceptRequest(book,user2);
        ArrayList<Exchange> exchangeList = user1.getExchangeList();
        Exchange exchange1 = exchangeList.get(exchangeList.size() - 1);
        Exchange exchange2 = new Exchange(user1,user2,"",false,book);
        assertEquals(exchange1, exchange2);
    }

    @Test
    public void test_rejectRequest() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.rejectRequest(book, user2);
    }

    @Test
    public void test_addOwnedBooks(){
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.addOwnedBook(book);
        ArrayList<Book> books = user1.getOwnedBookList();
        Book newBook = books.get(books.size() - 1);
        assertEquals(newBook,book);
    }

    @Test
    public void test_addRequestedBook() {
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user1);
        user1.addRequestedBook(request);
        ArrayList<Book> books = user1.getRequestedBooksList();
        Book newRequest = books.get(books.size() - 1);
        assertEquals(newRequest,request);
    }

    @Test
    public void test_returnBook() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book request = new Book("12345","Harry Potta","J.K. Rolling",user2);
        int requestID = request.getBookID();
        Exchange exchange = new Exchange(user2, user1, "test location", false, requestID);
        user1.returnBook(request.getBookID());

        assertEquals(Status.Available, request.getStatus());
    }

    @Test
    public void test_setLocation() {
        User user2 = new User("Gorib","0124-123-3456","mod@exm.com",
                "Sefat Ullah","Dhaka, Bangladesh");
        Book book = new Book("12345","Harry Potta","J.K. Rolling",user1);
        Exchange exchange = new Exchange(user1,user2,"",false,book);
        user1.setLocation(exchange, "Dhaka");
        assertEquals(user1.getLocation(exchange), "Dhaka");
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
        user1.removeOwnedBook(book);
        ArrayList<Book> books = user1.getOwnedBookList();
        Boolean x = false;
        if (books.contains(book)) {
            x = true;
        }
        assertTrue(x);
    }


    @Test
    public void test_editBook() {
        User user = new User ("username1", "780-111-2222",
                "email.yahoo.com", "name", "2132-52Ave");
        User user2 = new User("userName2", "780-111-4444",
                "email@gmailcom", "name2", "1111-25Ave");
        User user3 = new User("userName3", "780-111-6666",
                "email@hotmail.com", "name3", "1111-22Street");
        Book book1 = new Book("9876543211234", "harry Potter", "j k rowling");
        book1.setCurrentBorrower(user2);
        user.addOwnedBook(book1);               //add book to the list of owned book

        //edit some of the book descriptions
        book1.setCurrentBorrower(user3);        //edit the borrower of the book
        book1.setISBN("9876543211234");         //edit ISBN
        book1.setTitle("Harry Potter");         //edit title
        book1.setAuthor("J K Rowlings");        //edit author

        //check if book descriptions were edited successfully?
        assertEquals(user3, book1.getCurrentBorrower());
        assertEquals("J K Rowlings", book1.getAuthor());
        assertEquals("Harry Potter", book1.getTitle());
    }


    @Test
    public void test_editProfile() {
        User user = new User("username1", "780-111-2222",
                "email.yahoo.com", "name", "2132-52Ave");

        //edit contact info
        user.setEmail("email@gmailcom");
        user.setUserName("user_name");
        user.setPhoneNumber("780-222-4444");

        //check if contact info was edited successfully
        assertEquals("email@gmail.com", user.getEmail());
        assertEquals("780-222-4444", user.getPhoneNumber());
        assertEquals("user_name", user.getUserName());
    }
}