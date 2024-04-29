package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.helmo.projetmobile.database.TransfereRepository
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import be.helmo.projetmobile.viewmodel.AccountListViewModel

class TransferViewModelFactory(
    private val transfereRepository: TransfereRepository,
    private val currencyViewModel: CurrencyViewModel,
    private val accountViewModel: AccountListViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransferViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransferViewModel(transfereRepository, currencyViewModel, accountViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
