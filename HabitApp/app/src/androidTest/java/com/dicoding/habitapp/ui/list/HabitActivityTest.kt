package com.dicoding.habitapp.ui.list

import com.dicoding.habitapp.R
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@RunWith(AndroidJUnit4ClassRunner::class)
class HabitActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(HabitListActivity::class.java)
    }

    @Test
    fun clickAddHabitButton() {
        onView(withId(R.id.fab)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.fab)).perform(ViewActions.click())
        onView(withId(R.id.add_ed_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_ed_minutes_focus)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.sp_priority_level)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_tv_start_time)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}