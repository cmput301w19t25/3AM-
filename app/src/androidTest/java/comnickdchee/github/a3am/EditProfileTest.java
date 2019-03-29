package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class EditProfileTest {

    @Rule
    public ActivityTestRule<HomepageActivity> myActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity myActivity = null;
    private Solo soloprofile;

    @Before
    public void setUp() throws Exception {
        soloprofile = new Solo(getInstrumentation(), myActivityTestRule.getActivity());
    }

    @Test
    public void testEditProfile() {

        soloprofile.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        soloprofile.clickOnImageButton(0);

        //checks for the "my book navigation
        assertTrue(soloprofile.searchText("Profile"));

        soloprofile.clickOnImageButton(0);
        soloprofile.clickOnMenuItem("Profile");

        soloprofile.waitForFragmentById(R.id.profile_frag);

        soloprofile.clickOnView(soloprofile.getView(R.id.editFAB));
        soloprofile.waitForView(R.id.editprofile);

        View view = soloprofile.getView(R.id.editprofile);


        //View email = soloprofile.getView(R.id.emailEditText);
        EditText email = view.findViewById(R.id.emailEditText);
        soloprofile.clearEditText(email);
        soloprofile.enterText(email,"test@gmail.com");

        EditText phone = view.findViewById(R.id.phoneNumberEdit);
        soloprofile.clearEditText(phone);
        soloprofile.enterText(phone,"7809809999");

        EditText address = view.findViewById(R.id.addressEdit);
        soloprofile.clearEditText(address);
        soloprofile.enterText(address,"11009 9 Ave NW");

        //soloprofile.clickOnButton(R.id.saveButton);
        soloprofile.clickOnView(soloprofile.getView(R.id.saveButton));





    }

    @After
    public void tearDown() throws Exception {
        myActivity = null;
    }

}
