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

@RunWith(AndroidJUnit4.class)
    public class homepageActivityTest extends ActivityTestRule<HomepageActivity> {

            private Solo solo;

            public homepageActivityTest() {
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
        public void start() throws Exception{
            Activity activity = rule.getActivity();
        }

        @Test
        public void checkMenuItems() {
            solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

            solo.clickOnActionBarHomeButton();
            solo.clickLongOnText("Message");
            assertTrue(solo.waitForFragmentById(R.layout.fragment_message));
            assertTrue(solo.searchText("Message"));

            solo.clickOnActionBarHomeButton();
            solo.clickLongOnText("My Books");
            assertTrue(solo.waitForFragmentById(R.layout.fragment_borrowed));
            //assertTrue(solo.searchText("My Books"));

            solo.clickOnActionBarHomeButton();
            solo.clickLongOnText("Edit Profile");
            assertTrue(solo.waitForFragmentById(R.layout.fragment_profile));
            //assertTrue(solo.searchText("Edit Profile"));


            solo.clickOnActionBarHomeButton();
            solo.clickLongOnText("Logout");
            assertTrue(solo.waitForFragmentById(R.layout.fragment_logout));
            assertTrue(solo.searchText("Logout"));

            solo.clickOnActionBarHomeButton();
            solo.clickLongOnText("Home");
            assertTrue(solo.waitForFragmentById(R.layout.fragment_home));
            assertTrue(solo.searchText("BORROWED"));

            //solo.clickOnActionBarItem(nav_books);
            //assertTrue(solo.waitForFragmentByTag("Message Fragment"));
            //assertTrue(solo.searchText("Message Fragment"));

            //solo.waitForFragmentById(nav_message)
            //assertTrue(solo.isToggleButtonChecked("toggle"));


            //solo.clickOnButton("Clear");

            solo.clickOnActionBarHomeButton();
            solo.clickOnMenuItem("Message");
            assertTrue(solo.waitForFragmentById(4));
            assertTrue(solo.searchText("Message Fragment"));


        }



    }
