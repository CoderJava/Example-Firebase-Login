package com.codepolitan.examplefirebaselogin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.codepolitan.examplefirebaselogin.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by root on 03/03/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void ensureLoginWork() throws InterruptedException {
        onView(withId(R.id.edit_text_username_activity_main))
                .perform(typeText("kolonel.yudisetiawan@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_activity_main))
                .perform(typeText("nasigoreng"), closeSoftKeyboard());
        onView(withId(R.id.button_login_activity_main))
                .perform(click());
        Thread.sleep(30 * 1000);
    }

    @Test
    public void ensureOpenFormSignupWork() throws InterruptedException {
        onView(withId(R.id.button_sign_up_activity_main))
                .perform(click());
        Thread.sleep(5 * 1000);
    }

}