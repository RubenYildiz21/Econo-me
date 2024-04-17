package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(categoryId: UUID): ViewModel() {
    private val categoryRepository = CategoryRepository.get()

    private val _category: MutableStateFlow<Category?> = MutableStateFlow(null)
    val category : StateFlow<Category?> = _category.asStateFlow()

    init {
        viewModelScope.launch {
            val category = categoryRepository.getCategory(categoryId)
            _category.value = category
        }
    }

    fun updateCategory(onUpdate: (Category) -> Category) {
        _category.update {oldCategory ->
            oldCategory?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        category.value?.let { categoryRepository.updateCategory(it)  }
    }

}

class CategoryViewModelFactory(private val categoryId: UUID): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryId) as T
    }

}