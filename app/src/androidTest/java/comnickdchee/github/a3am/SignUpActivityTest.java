package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import android.support.test.runner.AndroidJUnit4;

import static org.junit.Assert.*;

public class SignUpActivityTest {
    @Rule
    public ActivityTestRule<SignUpActivity> nActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);

    private SignUpActivity nActivity = null;

    @Before //before you execute the test
    public void setUp() throws Exception {
        nActivity = nActivityTestRule.getActivity();

    }


    @Test
    public void testlaunch() {

        View view = nActivity.findViewById(R.id.PasswordReg);
        assertNotNull(view);
    }


    @After // after executing the test case
    public void tearDown() throws Exception {

        nActivity= null;

    }
}