package comnickdchee.github.a3am;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookUnitTests {

    @Test
    public void test_getISBN() {
        Book b = new Book("1234567891234", "HarryPotter", "J R R martin");
        assertEquals(b.getISBN().length(), 13);
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
}
