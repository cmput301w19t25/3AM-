package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class ViewBookActivityTest {

    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity mActivity = null;
    private Solo solobooksview;
    private ViewGroup tabs;



    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        solobooksview = new Solo(getInstrumentation(), mActivityTestRule.getActivity());


    }

    @Test
    public void testNavViewBooks() {

        solobooksview.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solobooksview.waitForFragmentById(R.id.pagerHomepage);

        assertTrue(solobooksview.searchText("Actions"));
        //solobooksview.waitForView(R.id.nav_view);
        //ViewGroup tabs = solobooksview.getView(android.R.id.pagerHomepage);
        //View actionstab = tabs.getChildAt(2);
        //solobooksview.clickOnView(actionstab);
        ///solobooksview.clickOnView(navto);


    }

        @After
        public void tearDown () throws Exception {
        }
    }
