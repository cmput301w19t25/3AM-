package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class HomeFragmentTest {


    @Rule
    public ActivityTestRule<HomepageActivity> rule =
            new ActivityTestRule<HomepageActivity>(HomepageActivity.class);

    private HomepageActivity homeActivity = null;


    @Before
    public void setUp() throws Exception {

        homeActivity = rule.getActivity();
    }

    @Test
    public void testLaunch() {

        //test if fragment is launched or not

        FrameLayout rlContainer = homeActivity.findViewById(R.layout.fragment_home);//fragment_container);

        assertNotNull(rlContainer);

        HomeFragment fragment = new HomeFragment();

        homeActivity.getSupportFragmentManager().beginTransaction().add(rlContainer.getId(), fragment).commitAllowingStateLoss();

        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewWithTag(0); //findViewById(R.id.pagerHomepage) // (R.id.pagerHomepage);
        assertNotNull(view);

        View view1 = fragment.getView().findViewWithTag(1);
        assertNotNull(view1);

        View view2 = fragment.getView().findViewWithTag(2);
        assertNotNull(view2);

        View view3 = fragment.getView().findViewWithTag(3);
        assertNotNull(view3);

    }

    @After
    public void tearDown() throws Exception {
    }
}