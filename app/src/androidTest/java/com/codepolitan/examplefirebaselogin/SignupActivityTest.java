package com.codepolitan.examplefirebaselogin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.codepolitan.examplefirebaselogin.signup.SignupActivity;

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
public class SignupActivityTest {

    @Rule
    public ActivityTestRule<SignupActivity> activityTestRule = new ActivityTestRule<SignupActivity>(SignupActivity.class);

    @Test
    public void ensureSignupWork() throws InterruptedException {
        onView(withId(R.id.edit_text_email_activity_signup))
                .perform(typeText("kolonel.yudisetiawan@yahoo.co.id"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_activity_signup))
                .perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.button_next_activity_signup))
                .perform(click());
        Thread.sleep(60 * 1000);
    }

    @Test
    public void ensureValidationWork() throws InterruptedException {
        onView(withId(R.id.edit_text_email_activity_signup))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.button_next_activity_signup))
                .perform(click());
        Thread.sleep(5 * 1000);
        onView(withId(R.id.edit_text_email_activity_signup))
                .perform(typeText("dianika.wahyuni@yahoo.co.id"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_activity_signup))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.button_next_activity_signup))
                .perform(click());
        Thread.sleep(5 * 1000);
    }

}