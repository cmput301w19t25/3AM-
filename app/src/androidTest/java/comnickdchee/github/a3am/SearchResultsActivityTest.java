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
import android.support.v7.widget.SearchView;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class SearchResultsActivityTest {
    @Rule
    public ActivityTestRule<HomepageActivity> mActivityTestRule = new ActivityTestRule<HomepageActivity>(HomepageActivity.class);
    private HomepageActivity mActivity = null;
    private Solo solo;


    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
        solo = new Solo(getInstrumentation(), mActivityTestRule.getActivity());


    }

    @Test
    public void testSearchView() {

        solo.assertCurrentActivity("Wrong Activity", HomepageActivity.class);

        solo.clickOnView(solo.getView(R.id.search));
        solo.enterText(0, "Harry Potter");
        solo.sleep(800);
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(10000);

    }

        @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}
