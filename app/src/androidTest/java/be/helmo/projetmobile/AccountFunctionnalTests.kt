package be.helmo.projetmobile


import android.view.View
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.helmo.projetmobile.database.CompteDao
import be.helmo.projetmobile.database.ProjectDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AccountFunctionnalTests {

    private lateinit var database: ProjectDatabase
    private lateinit var compteDao: CompteDao
    @Before
    fun setup() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java)
        // Initialisation de la base de données en mémoire
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, ProjectDatabase::class.java).build()
        compteDao = database.compteDao()
    }

    private fun addUserForTests() {
        Espresso.onView(ViewMatchers.withId(R.id.username))
            .perform(ViewActions.typeText("Trafalgar"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.user_lastname))
            .perform(ViewActions.typeText("Law"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.addUser))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.headerMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun navigateToAccountsPage() {
        Espresso.onView(ViewMatchers.withId(R.id.accounts))
            .perform(ViewActions.click())
    }

    @Test
    fun test01AddAccount_correctlyAddsAccount() {
        addUserForTests()
        navigateToAccountsPage()
        Espresso.onView(ViewMatchers.withId(R.id.addAccount))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.accountName))
            .perform(ViewActions.typeText("Nouveau Compte"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.accountCurrency))
            .perform(ViewActions.click())

        Espresso.onData(org.hamcrest.Matchers.anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .atPosition(0)
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.accountBalance))
            .perform(ViewActions.typeText("1000"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.addAccount))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.account_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Nouveau Compte"))))
    }

    @Test
    fun test02modifyAccount() {
        navigateToAccountsPage()
        Espresso.onView(ViewMatchers.withId(R.id.editAccount))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.accountName))
            .perform(ViewActions.replaceText("Compte Modifié"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.addAccount))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.account_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Compte Modifié"))))
    }

    @Test
    fun test03DeleteAccount() {
        navigateToAccountsPage()
        // Assumons que l'UI liste les comptes et qu'on clique sur le bouton de suppression pour un compte spécifique
        // Remplacez R.id.deleteAccountButton par l'identifiant réel du bouton de suppression dans votre application
        Espresso.onView(ViewMatchers.withId(R.id.deleteAccount))
            .perform(ViewActions.click())

        // Supposons que votre application affiche une boîte de dialogue de confirmation pour la suppression
        // Cliquez sur le bouton de confirmation de suppression
        // Remplacez R.id.confirmDeleteButton par l'identifiant réel du bouton de confirmation dans votre dialogue
        Espresso.onView(ViewMatchers.withText("Supprimer"))
            .perform(ViewActions.click())

        // Vérifier que le compte n'est plus listé dans la RecyclerView
        // Remplacez "Nouveau Compte" par le nom du compte que vous souhaitez vérifier
        Espresso.onView(ViewMatchers.withId(R.id.account_recycler_view))
            .check(ViewAssertions.matches(not(ViewMatchers.hasDescendant(ViewMatchers.withText("Nouveau Compte")))))
    }


    @After
    fun tearDown() {
        database.close()
    }
}
