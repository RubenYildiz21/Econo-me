package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.database.TransactionRepository

class HomeViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val currencyViewModel: CurrencyViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating HomeViewModel instance")
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(transactionRepository, categoryRepository, currencyViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
