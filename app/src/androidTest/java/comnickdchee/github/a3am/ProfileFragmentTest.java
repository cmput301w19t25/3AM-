package comnickdchee.github.a3am;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import comnickdchee.github.a3am.Activities.HomepageActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class ProfileFragmentTest {

    @Rule
    public IntentsTestRule<HomepageActivity> mActivityRule =
            new IntentsTestRule<HomepageActivity>(HomepageActivity.class);

    @Test
    public void checkEditProfileButton() {
        onView(withId(R.id.nav_profile)).perform(click());
        intended(hasComponent(ProfileFragment.class.getName()));
    }
}