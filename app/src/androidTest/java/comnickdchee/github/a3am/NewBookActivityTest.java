package comnickdchee.github.a3am;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;
import comnickdchee.github.a3am.Activities.NewBookActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class NewBookActivityTest {

    @Rule
    public ActivityTestRule<HomepageActivity> newActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity newActivity = null;
    private Solo solonewbook;

    @Before
    public void setUp() throws Exception {
        solonewbook = new Solo(getInstrumentation(), newActivityTestRule.getActivity());

    }

    @Test
    public void testHomePageNavToMyBooks() {

        solonewbook.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solonewbook.clickOnImageButton(0);

        assertTrue(solonewbook.searchText("My Books"));

        solonewbook.clickOnImageButton(0);
        solonewbook.clickOnMenuItem("My Books");

        solonewbook.clickOnView(solonewbook.getView(R.id.fabAddBookButton));

        solonewbook.waitForActivity(NewBookActivity.class);

        View view = solonewbook.getView(R.id.new_book_activity);

        onView(withId(R.id.tietBookTitle)).perform(ViewActions.typeText("New Title")).perform(closeSoftKeyboard());

        onView(withId(R.id.tietAuthor)).perform(ViewActions.typeText("New Author")).perform(closeSoftKeyboard());

        onView(withId(R.id.tietISBN)).perform(ViewActions.typeText("New ISBN")).perform(closeSoftKeyboard());

        onView(withId(R.id.ivFinishAddButton)).perform(click());


    }
        @After
    public void tearDown() throws Exception {
        newActivity = null;
    }
}
