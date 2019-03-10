package comnickdchee.github.a3am;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class MessageFragmentTest {


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

        FrameLayout rlContainer = (FrameLayout) homeActivity.findViewById(R.layout.fragment_message);//fragment_container);

        assertNotNull(rlContainer);

        MessageFragment fragment = new MessageFragment();

        homeActivity.getSupportFragmentManager().beginTransaction().add(rlContainer.getId(), fragment).commitAllowingStateLoss();

        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.fragment_container);   //view_in_fragment);

        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
    }
}