package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

public class zDeleteBookTest {
    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity mActivity = null;
    private Solo solo;


    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), mActivityTestRule.getActivity());


    }

    @Test
    public void testDeleteBook() {

        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnImageButton(0);

        //checks for the "my book navigation
        assertTrue(solo.searchText("My Books"));

        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("My Books");

        solo.waitForFragmentById(R.id.view_in_fragment);

        //solo.clickOnView(solo.getView(R.id.ibOption));

        onView(withId(R.id.ibOption)).perform(click());
        solo.clickOnMenuItem("Delete");
        solo.sleep(10000);

    }

    @After
    public void tearDown() throws Exception {

        mActivity= null;

    }
}