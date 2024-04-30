package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.database.TransfereRepository
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import be.helmo.projetmobile.viewmodel.AccountListViewModel

class TransactionViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val accountViewModel: AccountListViewModel,
    private val categoryViewModel: CategoryListViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionListViewModel(transactionRepository, accountViewModel, categoryViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}