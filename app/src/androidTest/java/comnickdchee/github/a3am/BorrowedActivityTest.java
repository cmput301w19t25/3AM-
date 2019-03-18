package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.BorrowedActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class BorrowedActivityTest {
    @Rule
    public ActivityTestRule<BorrowedActivity>bActivityTestRule = new ActivityTestRule<BorrowedActivity>(BorrowedActivity.class);


    private BorrowedActivity borrowed_Activity = null;

    @Before //before you execute the test
    public void setUp() throws Exception {
        borrowed_Activity = bActivityTestRule.getActivity();


    }


    @Test
    public void testlaunch_signin() {

        View appicon = borrowed_Activity.findViewById(R.id.RegisterBtn);
        assertNotNull(appicon);

    }


    @After // after executing the test case
    public void tearDown() throws Exception {

        borrowed_Activity= null;

    }
}