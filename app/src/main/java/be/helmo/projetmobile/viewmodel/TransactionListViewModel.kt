package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionListViewModel: ViewModel() {

    private val transactionRepository: TransactionRepository = TransactionRepository.get()

    private val _transaction: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    val transaction : StateFlow<List<Transaction>>
        get() = _transaction.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionRepository.getTransactions().collect { listOfTransac ->
                _transaction.value = listOfTransac
            }
        }
    }


    fun saveOrUpdateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            if (transaction.id > 0) {
                // L'ID existe, donc c'est une mise à jour
                transactionRepository.updateTransaction(transaction)
            } else {
                // Pas d'ID, donc c'est une nouvelle entrée
                transactionRepository.addTransaction(transaction)
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
            loadTransactions()  // Refresh the list after deletion
        }
    }
}