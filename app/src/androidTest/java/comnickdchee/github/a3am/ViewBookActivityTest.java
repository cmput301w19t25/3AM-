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
import comnickdchee.github.a3am.Fragments.ActionsFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class ViewBookActivityTest {

    //private static final Object ActionsFragment = ;
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

        //solobooksview.waitForFragmentById(R.id.pagerHomepage);

        assertTrue(solobooksview.searchText("Actions"));

        solobooksview.clickOnText("Lending");
        solobooksview.sleep(200);

        solobooksview.clickOnText("Actions");
        solobooksview.sleep(200);

        solobooksview.clickOnText("Borrowed");
        solobooksview.sleep(200);

        solobooksview.clickOnText("Requests");
        solobooksview.sleep(800);



    }

        @After
        public void tearDown () throws Exception {
        mActivity = null;
        }
    }
