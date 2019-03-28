package comnickdchee.github.a3am;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import android.app.Instrumentation;

import com.robotium.solo.Solo;

import org.junit.runner.RunWith;

import comnickdchee.github.a3am.Activities.HomepageActivity;
import comnickdchee.github.a3am.Activities.NewBookActivity;
import comnickdchee.github.a3am.Fragments.MyBooksFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class MyBooksFragmentTest  {

    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity mActivity = null;
    private Solo solo;


    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        solo = new Solo(getInstrumentation(), mActivityTestRule.getActivity());


    }

    @Test
    public void testHomePageNavToMyBooks() {

        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnImageButton(0);

        //checks for the "my book navigation
        assertTrue(solo.searchText("My Books"));

        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("My Books");

        solo.waitForFragmentById(R.id.view_in_fragment);
        //solo.assertCurrentActivity("Wrong Activity", MyBooksFragment.class);
        solo.clickOnButton(0);
        solo.clickOnMenuItem("Available");

        solo.clickOnButton(0);
        solo.clickOnMenuItem("Borrowed");

        solo.clickOnButton(0);
        solo.clickOnMenuItem("Requested");

        solo.clickOnButton(0);
        solo.clickOnMenuItem("Accepted");

        //View fab = findViewById(R.id.fabAddBookButton);
        solo.clickOnView(solo.getView(R.id.fabAddBookButton));

        //solo.waitForView(R.id.new_book_activity);
        solo.waitForActivity(NewBookActivity.class);
        SystemClock.sleep(5000);


    }

    @After
    public void tearDown() throws Exception {
       mActivity = null;
    }
}
