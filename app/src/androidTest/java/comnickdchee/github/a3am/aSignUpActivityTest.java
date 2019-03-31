package comnickdchee.github.a3am;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import comnickdchee.github.a3am.Activities.SignInActivity;
import comnickdchee.github.a3am.Activities.SignUpActivity;

/**
 * @author Tatenda
 * uses expresso for testing the signup
 */

public class aSignUpActivityTest {
    @Rule
    public ActivityTestRule<SignUpActivity> nActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);
    //public ActivityTestRule<SignInActivity> bActivityTestRule = new ActivityTestRule<SignInActivity>(SignUpActivity.class);

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private SignUpActivity nActivity = null;
    private Solo solo;
    //private SignUpActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SignInActivity.class.getName(),null,false);//monitor to show when second activity opens

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
        onView(withId(R.id.userName)).perform(typeText("one113")).perform(closeSoftKeyboard());
        onView(withId(R.id.EmailReg)).perform(typeText("one113@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.PasswordReg)).perform(typeText("one1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("421 NY")).perform(closeSoftKeyboard());
        onView(withId(R.id.phoneNumber)).perform(typeText("7778904605")).perform(closeSoftKeyboard());
       // SystemClock.sleep(3000);

    }

}