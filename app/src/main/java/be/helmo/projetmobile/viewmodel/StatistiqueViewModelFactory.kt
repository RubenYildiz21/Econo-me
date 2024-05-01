package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.database.TransfereRepository

class StatistiqueViewModelFactory(
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatistiqueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatistiqueViewModel(categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
