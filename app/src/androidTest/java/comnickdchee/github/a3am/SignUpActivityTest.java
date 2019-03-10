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

public class SignUpActivityTest {
    @Rule
    public ActivityTestRule<SignUpActivity> nActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);
    //public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);


    private SignUpActivity nActivity = null;
    //private SignUpActivity mActivity = null;

    //Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(BorrowedActivity.class.getName(),null,false);//monitor to show when second activity opens

    @Before //before you execute the test
    public void setUp() throws Exception {
        nActivity = nActivityTestRule.getActivity();


    }


    @Test
    public void testlaunch() {

        View passwordField = nActivity.findViewById(R.id.PasswordReg);
        assertNotNull(passwordField);

    }

    //@Test
   // public void TestLaunchofsigninwhenbuttonisclicked(){
        //mActivity = nActivityTestRule.getActivity();
        //assertNotNull(mActivity.findViewById(R.id.RegisterConfirm));
        //onView(withId(R.id.userName)).perform(typeText("myself"));
        //onView(withId(R.id.EmailReg).perform(typeText("myself@ualberta.ca")));
        //onView(withId(R.id.PasswordReg)).perform(typeText("myself1234"));
        //onView(withId(R.id.address)).perform(typeText("421 Edinburgh"));
        //onView(withId(R.id.phoneNumber)).perform(typeText("0777890465")),closeSoftKeyboard();




        //onView(withId(R.id.RegisterConfirm)).perform(click());
        //Activity signinActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000); //timeout is in milli-seconds
        //assertNotNull(signinActivity);
        //BorrowedActivity.finish();
    //}


    @After // after executing the test case
    public void tearDown() throws Exception {

        nActivity= null;

    }
}