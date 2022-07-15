import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeActivity
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.junit.Before

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun clickAddTaskButton() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed()))
        onView(withId(R.id.action_add)).perform(click())
        onView(withId(R.id.add_course_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_start_time)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_end_time)).check(matches(isDisplayed()))
        onView(withId(R.id.add_lecturer_title)).check(matches(isDisplayed()))
        onView(withId(R.id.add_note)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerDay)).check(matches(isDisplayed()))
    }
}
