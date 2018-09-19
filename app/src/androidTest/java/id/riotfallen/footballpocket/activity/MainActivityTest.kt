package id.riotfallen.footballpocket.activity

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import id.riotfallen.footballpocket.R.id.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun textAppBehaviour() {
        Thread.sleep(5000)

        onView(withId(mainRecyclerViewNextEvent))
                .check(matches(isDisplayed()))

        onView(withId(mainRecyclerViewNextEvent))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))

        onView(withId(mainRecyclerViewNextEvent))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        Thread.sleep(5000)

        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))

        onView(withId(add_to_favorite)).perform(click())

        Thread.sleep(1000)

        pressBack()

        Thread.sleep(1000)

        onView(withId(mainActivityLinearLayoutLeaguePicker))
                .check(matches(isDisplayed()))

        onView(withId(mainActivityLinearLayoutLeaguePicker))
                .perform(click())

        Thread.sleep(1000)

        onView(withText("Spanish La Liga"))
                .check(matches(isDisplayed()))

        onView(withText("Spanish La Liga"))
                .perform(click())

        Thread.sleep(3000)

        onView(withId(mainRecyclerViewFavoriteEvent))
                .check(matches(isDisplayed()))

        onView(withId(mainRecyclerViewFavoriteEvent))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))

        onView(withId(mainRecyclerViewFavoriteEvent))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(5000)

        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))

        onView(withId(add_to_favorite)).perform(click())

        Thread.sleep(1000)

        pressBack()

        Thread.sleep(5000)

        onView(withId(mainRecyclerViewFavoritePlayer))
                .check(matches(isDisplayed()))

        onView(withId(mainRecyclerViewFavoritePlayer))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))

        onView(withId(mainRecyclerViewFavoritePlayer))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))
        Thread.sleep(3000)

        pressBack()

        Thread.sleep(2000)
    }

}