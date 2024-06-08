package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_Success() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("malih17@gmail.com"), closeSoftKeyboard())

        onView(withId(R.id.passwordEditText))
            .perform(typeText("malih521"), closeSoftKeyboard())

        onView(withId(R.id.loginButton))
            .perform(click())

        onView(withText("You are logged in successfully"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText("Continue"))
            .perform(click())


        onView(withId(R.id.rv_story))
            .check(matches(isDisplayed()))
    }

    @Test
    fun login_Failed() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("malih17@gmail.comx"), closeSoftKeyboard())

        onView(withId(R.id.passwordEditText))
            .perform(typeText("malih521"), closeSoftKeyboard())

        onView(withId(R.id.loginButton))
            .perform(click())

        onView(withText("\"email\" must be a valid email"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText("Ok"))
            .perform(click())

    }
}