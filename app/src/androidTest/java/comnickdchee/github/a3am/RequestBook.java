package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class RequestBook {

    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity requestbook = null;
    private Solo solo;

    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        solo = new Solo(getInstrumentation(), mActivityTestRule.getActivity());
    }

    @Test
    public void testSearchView() {

        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnView(solo.getView(R.id.search));
        solo.enterText(0, "Here I stand"); //make new account and chance this to test borrow
        solo.sleep(800);
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(1000);

        solo.clickInList(0);

    }

    @After
    public void tearDown() throws Exception {

        requestbook= null;

    }
}
