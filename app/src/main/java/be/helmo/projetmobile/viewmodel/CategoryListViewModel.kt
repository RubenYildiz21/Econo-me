package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryListViewModel : ViewModel() {
    private val categoryRepository: CategoryRepository = CategoryRepository.get()

    private val _categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())
    val categories : StateFlow<List<Category>>
        get() = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getCategories().collect {
                _categories.value = it
            }
        }

        /**for (i in 0 until 5) {
            categories += Category(UUID.randomUUID(), "Category $i")
        }*/
    }

    suspend fun addCategory(name: String) : UUID {
        val category = Category(UUID.randomUUID(), name, 0.0)
        categoryRepository.addCategory(category)
        return category.id
    }
}