package be.helmo.projetmobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
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
class CategoryListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CategoryListViewModel
    private lateinit var categoryRepository: CategoryRepository
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        categoryRepository = mockk(relaxed = true)
        mockkObject(CategoryRepository)
        every { CategoryRepository.get() } returns categoryRepository

        viewModel = CategoryListViewModel()
    }

    @Test
    fun testLoadCategories() = testDispatcher.runBlockingTest {
        val expectedCategories = listOf(Category(1, "Food", 500.0))
        every { categoryRepository.getCategories() } returns flowOf(expectedCategories)

        viewModel.loadCats()

        advanceUntilIdle()

        assertEquals(expectedCategories, viewModel.categories.value)
    }

    @Test
    fun testAddCategory() = testDispatcher.runBlockingTest {
        val categoryName = "Entertainment"
        val newCategory = Category(0, categoryName, 0.0)
        viewModel.addCategory(categoryName)

        coVerify { categoryRepository.addCategory(newCategory) }
    }

    @Test
    fun testSaveOrUpdateCategory_NewCategory() {
        val newCategory = Category(0, "New Category", 0.0)

        viewModel.saveOrUpdateCategory(newCategory)

        coVerify { categoryRepository.addCategory(newCategory) }
    }

    @Test
    fun testSaveOrUpdateCategory_UpdateCategory() {
        val existingCategory = Category(1, "Existing Category", 1000.0)

        viewModel.saveOrUpdateCategory(existingCategory)

        coVerify { categoryRepository.updateCategory(existingCategory) }
    }

    @Test
    fun testDeleteCategory() {
        val categoryToDelete = Category(1, "Delete Me", 200.0)

        viewModel.deleteCat(categoryToDelete)

        coVerify { categoryRepository.deleteCat(categoryToDelete) }
        coVerify { categoryRepository.getCategories() }
    }

    @Test
    fun testUpdateCategoryAfterDelete() = testDispatcher.runBlockingTest {
        val initialCategory = Category(1, "Existing Category", 1000.0)
        viewModel.initAccountsForTest(listOf(initialCategory))

        val soldeChange = 200.0
        viewModel.updateCategoryAfterDelete(soldeChange, 1)

        advanceUntilIdle()

        val expectedSolde = 800.0  // 1000 - 200
        val updatedCategory = viewModel.categories.value.first { it.id == 1 }

        assertEquals(expectedSolde, updatedCategory.solde, 0.01)

        coVerify { categoryRepository.updateCategory(match {
            it.id == 1 && Math.abs(it.solde - expectedSolde) < 0.01
        }) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

}