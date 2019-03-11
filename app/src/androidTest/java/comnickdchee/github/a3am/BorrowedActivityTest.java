package comnickdchee.github.a3am;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
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

    @Test
    public void SigninWithInfo(){
        assertNotNull(borrowed_Activity.findViewById(R.id.LoginBtn));
        onView(withId(R.id.EmailReg)).perform(typeText("one113@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.PasswordReg)).perform(typeText("one1234")).perform(closeSoftKeyboard());

        onView(withId(R.id.LoginBtn)).perform(click());
    }


    @After // after executing the test case
    public void tearDown() throws Exception {

        borrowed_Activity= null;

    }
}