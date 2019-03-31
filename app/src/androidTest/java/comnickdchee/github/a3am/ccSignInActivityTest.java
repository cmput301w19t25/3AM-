package comnickdchee.github.a3am;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.SignInActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * @author Tatenda
 * use of expresso to test sign in
 */

public class ccSignInActivityTest {
    @Rule
    public ActivityTestRule<SignInActivity>bActivityTestRule = new ActivityTestRule<SignInActivity>(SignInActivity.class);

    private Solo solo;
    private SignInActivity borrowed_Activity = null;

    @Before //before you execute the test
    public void setUp() throws Exception {
        borrowed_Activity = bActivityTestRule.getActivity();


    }


    @Test
    public void testlaunch_signin() {

        View appicon = borrowed_Activity.findViewById(R.id.RegisterBtn);
        assertNotNull(appicon);
        onView(withId(R.id.EmailReg)).perform(typeText("tatendachivasa@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.PasswordReg)).perform(typeText("bigtate")).perform(closeSoftKeyboard());
        //solo.sleep(300);

        //solo.clickOnView(solo.getView(R.id.LoginBtn));
        //solo.clickOnButton("SIGN IN");


        onView(withId(R.id.LoginBtn)).perform(click());

        SystemClock.sleep(3000);

        //solo.waitForView(R.id.fragment_container);
        //onView(withText("Loading User Data")).check(matches(isDisplayed()));
        //SystemClock.sleep(800);

        //solo.waitForDialogToClose();
        //solo.sleep(3000);


    }


    @After // after executing the test case
    public void tearDown() throws Exception {

        borrowed_Activity= null;

    }
}
