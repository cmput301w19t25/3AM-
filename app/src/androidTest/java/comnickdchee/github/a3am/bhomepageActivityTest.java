package comnickdchee.github.a3am;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Asma
 * Tests for HomepageActivity; Navigation menu, menuItems and tabs.
 * Used Robotium for testing
 */

@RunWith(AndroidJUnit4.class)
    public class bhomepageActivityTest extends ActivityTestRule<HomepageActivity> {

    private Solo solo;

    public bhomepageActivityTest() {
        super(HomepageActivity.class, true, true);
    }

    @Rule
    public ActivityTestRule<HomepageActivity> rule =
            new ActivityTestRule<>(HomepageActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());

    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkMenuItems() {
        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnImageButton(0);

        //Test if there exists a Home button
        assertTrue(solo.searchText("Home"));

        //Test if there exists a "My Books" button
        assertTrue(solo.searchText("My Books"));

        //Test if there exists a "Edit Profile" button
        assertTrue(solo.searchText("Profile"));

        //Test if there exists a "Message" button
        assertTrue(solo.searchText("Message"));
        //assertTrue(solo.waitForFragmentById(R.layout.fragment_message));

        //Test if there exists a "Logout" button
        assertTrue(solo.searchText("Logout"));


        //Test if Home button from the menu is clickable
        solo.clickOnMenuItem("Home");

        //Test if My Books button from the menu is clickable
        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("My Books");

        //Test if Edit Profile button from the menu is clickable
        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("Profile");

        //Test if Message button from the menu is clickable
        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("Message");


        //Test if Logout button from the menu is clickable
        solo.clickOnImageButton(0);
        solo.clickOnMenuItem("Logout");

    }

    @Test
    public void testTabs() {

        /*ViewGroup tabs = (ViewGroup) solo.getView(R.id.pagerHomepage);
        View viewToView = tabs.getChildAt(2); //change x to the index you want.
        solo.clickOnView(viewToView);*/


        //solo.clickLongOnTextAndPress("ACTIONS", 2);


            /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                solo.clickOnActionBarItem(position);
            }
        });*/
    }

}
