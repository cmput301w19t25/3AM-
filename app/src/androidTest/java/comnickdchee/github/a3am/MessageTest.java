package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class MessageTest {

    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity m_book = null;
    private Solo solo;

    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        solo = new Solo(getInstrumentation(), mActivityTestRule.getActivity());
    }

    @Test
    public void testMessaging() {

        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnView(solo.getView(R.id.search));
        solo.enterText(0, "Here I stand"); //make new account and chance this to test borrow
        solo.sleep(800);

        solo.pressSoftKeyboardSearchButton();
        solo.sleep(800);

        solo.clickInRecyclerView(0);
        solo.sleep(1000);

        solo.waitForView(R.id.requestbook_view);
        solo.clickOnView(solo.getView(R.id.buttonViewProfile));

        //View firstview = solo.getView(R.id.message_buttonview);

        solo.clickOnButton("Message");

        View view = solo.getView(R.id.message_view);

        EditText message = view.findViewById(R.id.edittext_chatbox);
        //soloprofile.clearEditText(phone);
        solo.enterText(message,"Hello I would like to Borrow your book");

        solo.clickOnView(solo.getView(R.id.button_chatbox_send));
        solo.sleep(1000);


    }

    @After
    public void tearDown() throws Exception {

        m_book= null;

    }
}
