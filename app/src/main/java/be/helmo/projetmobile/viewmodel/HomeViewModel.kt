package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val transactionRepository: TransactionRepository, private val categoryRepository: CategoryRepository, private val currencyViewModel: CurrencyViewModel) : ViewModel() {
    private val _revenueData = MutableLiveData<List<Category>>()
    val revenueData: LiveData<List<Category>> = _revenueData

    private val _expenseData = MutableLiveData<List<Category>>()
    val expenseData: LiveData<List<Category>> = _expenseData

    init {
        // Fetch currencies once or refresh periodically
        currencyViewModel.fetchCurrencies("68bf113bb26cb8b6af82d7c07b8be2b3")

        // Observe changes in currency data and reload transactions
        currencyViewModel.currencies.observeForever {
            loadTransactions()
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            try {
                val transactions = transactionRepository.getTransactions().first()
                Log.d("HomeViewModel", "Transactions loaded: ${transactions.size}")

                // Use the latest rates from currencyViewModel
                val rates = currencyViewModel.currencies.value?.associate { it.code to it.name.toDouble() } ?: mapOf()
                val convertedTransactions = transactions.map { transaction ->
                    val rate = rates[transaction.devise] ?: 1.0  // Default to 1.0 if the rate is not found
                    transaction.copy(solde = transaction.solde * rate)
                }
                processTransactions(convertedTransactions.filter { it.type }, _revenueData)
                processTransactions(convertedTransactions.filterNot { it.type }, _expenseData)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading transactions: ${e.message}")
            }
        }
    }


    private suspend fun processTransactions(
        transactions: List<Transaction>,
        liveData: MutableLiveData<List<Category>>
    ) {
        val categories = transactions.groupBy { it.categoryId }.map { (categoryId, trans) ->
            Category(
                id = categoryId,
                nom = getCategoryNameById(categoryId),
                solde = trans.sumOf { it.solde }
            )
        }
        liveData.postValue(categories)
    }


    private suspend fun getCategoryNameById(categoryId: Int): String {
        Log.d("HomeViewModel", "Fetched category: ${categoryRepository.getCategory(categoryId).nom}")
        return categoryRepository.getCategory(categoryId).nom

    }
}
