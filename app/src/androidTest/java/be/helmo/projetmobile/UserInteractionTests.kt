package be.helmo.projetmobile


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserInteractionTests {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addUser_whenDetailsAreCorrect_navigatesToHome() {
        // Navigate to the LoginFragment if needed

        // Input user name
        onView(withId(R.id.username)).perform(typeText("Trafalgar"), closeSoftKeyboard())  // Ensure you use the correct ID for the user name input field

        // Input last name
        onView(withId(R.id.user_lastname)).perform(typeText("Law"), closeSoftKeyboard())  // Ensure you use the correct ID for the last name input field

        // Click on the Add User button
        onView(withId(R.id.addUser)).perform(click())

        // Verification if the app navigates to the HomeFragment after adding the user
        // Assuming HomeFragment shows a welcome message or a specific element that can be verified
        onView(withId(R.id.headerMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun updateUserProfile_updatesDataCorrectly_andShowsConfirmation() {
        // Assume that navigating to the UserFragment requires clicking on a header button.
        onView(withId(R.id.headerButton)).perform(click())

        // Entering new details in the profile update fields
        onView(withId(R.id.user_name)).perform(typeText("Roronoa"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.user_lastname)).perform(typeText("Zoro"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.updateUser)).perform(click())

        // Vérifier que les nouvelles informations de profil sont affichées
        onView(withId(R.id.header_username)).check(matches(withText("Roronoa Zoro")))
    }

}


