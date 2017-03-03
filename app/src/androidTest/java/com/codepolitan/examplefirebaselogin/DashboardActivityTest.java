package com.codepolitan.examplefirebaselogin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by root on 03/03/17.
 */
@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> activityTestRule = new ActivityTestRule<DashboardActivity>(DashboardActivity.class);

    @Test
    public void ensureOpenFormNewData() throws InterruptedException {
        onView(withId(R.id.fab_new_dashboard))
                .perform(click());
        Thread.sleep(10*1000);
    }

}