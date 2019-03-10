package comnickdchee.github.a3am;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

public class BorrowedActivityTest {
    @Rule
    public ActivityTestRule<BorrowedActivity>borrowedActivityTestRule = new ActivityTestRule<BorrowedActivity>(BorrowedActivity.class);


    private BorrowedActivity borrowed_Activity = null;

    @Before //before you execute the test
    public void setUp() throws Exception {
        borrowed_Activity = borrowedActivityTestRule.getActivity();


    }


    @Test
    public void testlaunch_signin() {

        View passwordField = borrowed_Activity.findViewById(R.id.imageView3);
        assertNotNull(passwordField);

    }

    @After // after executing the test case
    public void tearDown() throws Exception {

        borrowed_Activity= null;

    }
}