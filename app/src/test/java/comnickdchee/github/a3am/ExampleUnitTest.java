package comnickdchee.github.a3am;

import android.media.Image;
import android.support.annotation.DrawableRes;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    Book b = new Book("1234567891234", "HarryPotter", "J R R martin");

    @Test
    public void ISBN_correct() {
        {
            assertEquals(b.getISBN().length(), 13);
        }
    }
    @Test
    public void getAuthorName() {
        {
            assertEquals(b.getAuthor(), "J R R martin");
        }
    }
    @Test
    public void getTitle() {
        {
            assertEquals(b.getTitle(), "HarryPotter");
        }
    }
    @Test
    public void setAuthorName() {
        {
            b.setAuthor("J K Rowlin");
            assertEquals("J K Rowlin", b.getAuthor());
        }
    }
    @Test
    public void setTitle() {
        {
            b.setTitle("Harry");
            assertEquals("Harry", b.getTitle());
        }
    }

}