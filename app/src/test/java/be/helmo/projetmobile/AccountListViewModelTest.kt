package be.helmo.projetmobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.database.ProjectDatabase
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AccountListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AccountListViewModel
    private lateinit var compteRepository: CompteRepository
    private lateinit var database: ProjectDatabase
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        // Créer les mocks
        database = mockk(relaxed = true)
        compteRepository = mockk(relaxed = true)

        // Mocking des objets statiques
        mockkObject(ProjectDatabase)
        mockkObject(CompteRepository)

        every { ProjectDatabase.get() } returns database
        every { CompteRepository.get() } returns compteRepository
        every { database.compteDao() } returns mockk(relaxed = true)

        // Initialisation du ViewModel avec le repository mocké
        viewModel = AccountListViewModel()
    }


    @Test
    fun testLoadComptes() = testDispatcher.runBlockingTest {
        val expectedComptes = listOf(Compte(1, "Savings", "EUR", 1500.0))
        every { compteRepository.getComptes() } returns flowOf(expectedComptes)

        viewModel.loadComptes()

        advanceUntilIdle()  // Avancer le temps jusqu'à ce que toutes les coroutines soient terminées

        assertEquals(expectedComptes, viewModel.accounts.value)
    }

    @Test
    fun testSaveOrUpdateAccount_NewAccount() {
        val newAccount = Compte(0, "New Account", "USD", 100.0)

        viewModel.saveOrUpdateAccount(newAccount)

        coVerify { compteRepository.addCompte(newAccount) }
    }

    @Test
    fun testSaveOrUpdateAccount_UpdateAccount() {
        val existingAccount = Compte(1, "Existing Account", "EUR", 200.0)

        viewModel.saveOrUpdateAccount(existingAccount)

        coVerify { compteRepository.updateCompte(existingAccount) }
    }

    @Test
    fun testDeleteAccount() {
        val accountToDelete = Compte(1, "Delete Me", "USD", 300.0)

        viewModel.deleteAccount(accountToDelete)

        coVerify { compteRepository.deleteCompte(accountToDelete) }
        // Vérifie également que les comptes sont rechargés après la suppression
        coVerify { compteRepository.getComptes() }
    }

    @Test
    fun testUpdateAccountAfterDelete_UpdatesSoldeCorrectly() = testDispatcher.runBlockingTest {
        // Initialiser le MutableStateFlow avec un compte existant
        val initialAccount = Compte(1, "Existing Account", "USD", 1000.0)
        viewModel.initAccountsForTest(listOf(initialAccount))

        val soldeChange = 200.0
        val revenu = true  // Si vrai, cela devrait réduire le solde, ajustez cette logique selon votre définition

        // Act: perform the update
        viewModel.updateAccountAfterDelete(soldeChange, 1, revenu)

        // Attendre que toutes les coroutines lancées par ViewModel soient terminées
        advanceUntilIdle()

        // Vérifier que le solde a été correctement mis à jour
        val expectedSolde = 800.0
        val updatedAccount = viewModel.accounts.value.first { it.id == 1 }

        assertEquals(expectedSolde, updatedAccount.solde, 0.01)

        // Vérifier que la méthode de mise à jour du compte a été appelée
        coVerify { compteRepository.updateCompte(match {
            it.id == 1 && Math.abs(it.solde - expectedSolde) < 0.01
        }) }
    }


    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain() // Reset le Main Dispatcher après les tests
    }


}