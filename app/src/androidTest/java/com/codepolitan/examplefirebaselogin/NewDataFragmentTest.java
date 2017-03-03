package com.codepolitan.examplefirebaselogin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by root on 03/03/17.
 */
@RunWith(AndroidJUnit4.class)
public class NewDataFragmentTest {

    @Rule
    public ActivityTestRule<DashboardActivity> activityTestRule = new ActivityTestRule<DashboardActivity>(DashboardActivity.class);

    @Test
    public void ensureCreateNewDataWork() throws InterruptedException {
        onView(withId(R.id.fab_new_dashboard))
                .perform(click());
        onView(withId(R.id.edit_text_title_fragment_new_data))
                .perform(typeText("My Experience in Android Development Test"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_content_fragment_new_data))
                .perform(typeText("Hello," +
                        "\nMy name is Yudi Setiawan. In this note, I will tell you about my experience in Android Development Test with YSN Studio"), closeSoftKeyboard());
        onView(withId(R.id.fab_save_fragment_new_data))
                .perform(click());
        Thread.sleep(60 * 1000);
    }

}