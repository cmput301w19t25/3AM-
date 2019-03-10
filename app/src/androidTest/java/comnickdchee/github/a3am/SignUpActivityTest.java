package comnickdchee.github.a3am;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Models.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SignUpActivityTest {
    @Rule
    public ActivityTestRule<SignUpActivity> nActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);
    //public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    private SignUpActivity nActivity = null;
    //private SignUpActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(BorrowedActivity.class.getName(),null,false);//monitor to show when second activity opens

    @Before //before you execute the test
    public void setUp() throws Exception {
        nActivity = nActivityTestRule.getActivity();
        //mActivity = mActivityTestRule.getActivity();


    }


    @Test
    public void testlaunch() {

        View passwordField = nActivity.findViewById(R.id.PasswordReg);
        assertNotNull(passwordField);

    }

    @Test
    public void TestLaunchofsigninwhenbuttonisclicked(){
        SystemClock.sleep(800);
        assertNotNull(nActivity.findViewById(R.id.RegisterConfirm));
        onView(withId(R.id.userName)).perform(typeText("two"));
        onView(withId(R.id.EmailReg)).perform(typeText("two@gmail.com"));
        onView(withId(R.id.PasswordReg)).perform(typeText("one1234"));
        onView(withId(R.id.address)).perform(typeText("421 NY"));
        onView(withId(R.id.phoneNumber)).perform(typeText("7778904605")).perform(closeSoftKeyboard());
        //Thread.sleep(250)

        onView(withId(R.id.RegisterConfirm)).perform(click());

       // Activity signinActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000); //timeout is in milli-seconds
        Activity BorrowedActivity = getInstrumentation().waitForMonitor(monitor);
        assertNotNull(BorrowedActivity);
        //BorrowedActivity.finish();
    }


    @After // after executing the test case
    public void tearDown() throws Exception {

        //User
        mAuth.getCurrentUser().delete();
        nActivity= null;
       // mActivity = null;

    }
}