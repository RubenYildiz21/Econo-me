package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(private val transactionRepository: TransactionRepository, private val categoryRepository: CategoryRepository, private val currencyViewModel: CurrencyViewModel) : ViewModel() {
    private val _revenueData = MutableLiveData<List<Category>>()
    val revenueData: LiveData<List<Category>> = _revenueData

    private val _expenseData = MutableLiveData<List<Category>>()
    val expenseData: LiveData<List<Category>> = _expenseData

    private val _revenueSum = MutableLiveData<Double>()
    val revenueSum: LiveData<Double> = _revenueSum

    private val _expenseSum = MutableLiveData<Double>()
    val expenseSum: LiveData<Double> = _expenseSum
    init {
        loadCategoryData(true)
        loadCategoryData(false)
        loadFinancialSummary()
    }

    private fun loadCategoryData(type: Boolean) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "entry loadCategoryData()")
            try {
                categoryRepository.getTransactionType(type).collect { categories ->
                    Log.d("HomeViewModel", "Revenue categories loaded: ${categories.size}")
                    if (categories.isNotEmpty() && type){
                        _revenueData.postValue(categories)
                    }else{
                        _expenseData.postValue(categories)
                        Log.d("HomeViewModel", "No revenue data available")
                    }

                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading categories: ${e.message}")
            }
        }
    }

    private fun loadFinancialSummary() {
        viewModelScope.launch {
            try {
                // Calcul de la somme des revenus comme la somme des soldes des comptes
                val allAccounts = transactionRepository.getAccounts().first()
                val accountSum = allAccounts.sumOf { account ->
                    convertCurrencyToEUR(account.solde, account.devise)
                }

                // Met à jour la somme des revenus dans LiveData
                _revenueSum.postValue(accountSum)

                // Récupération des transactions pour le mois en cours
                val currentMonth = LocalDate.now().monthValue.toString().padStart(2, '0')
                Log.d("HomeViewModel", "month : $currentMonth")
                val transactionsThisMonth = transactionRepository.getTransactionsByMonth(currentMonth).first()
                Log.d("HomeViewModel", "transactionsThisMonth : ${transactionsThisMonth.filter { it.type }}")

                // Filtrage des transactions pour obtenir uniquement les dépenses
                val expenseSum = transactionsThisMonth.filter { !it.type }.sumOf { transaction ->
                    convertCurrencyToEUR(transaction.solde, transaction.devise)
                }

                // Met à jour la somme des dépenses dans LiveData
                _expenseSum.postValue(expenseSum)

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading financial summary: ${e.message}")
            }
        }
    }

    private suspend fun convertCurrencyToEUR(amount: Double, currency: String): Double {
        return if (currency != "EUR") {
            val rate = currencyViewModel.getExchangeRate(currency, "EUR")
            (amount * rate)
        } else {
            amount
        }
    }
}
