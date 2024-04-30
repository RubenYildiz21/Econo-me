package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
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
    }

    suspend fun addCategory(name: String) : Int {
        val category = Category(0, name, 0.0)
        categoryRepository.addCategory(category)
        return category.id
    }

    fun saveOrUpdateCategory(category: Category) {
        viewModelScope.launch {
            if (category.id > 0) {
                // L'ID existe, donc c'est une mise à jour
                categoryRepository.updateCategory(category)
            } else {
                // Pas d'ID, donc c'est une nouvelle entrée
                categoryRepository.addCategory(category)
            }
        }
    }

    fun deleteCat(cat: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCat(cat)
            loadCats()  // Refresh the list after deletion
        }
    }

    private fun loadCats() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { listOfComptes ->
                _categories.value = listOfComptes
            }
        }
    }
}