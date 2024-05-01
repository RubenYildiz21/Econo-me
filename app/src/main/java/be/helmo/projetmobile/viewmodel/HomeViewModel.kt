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
        loadCategoryData(true)
        loadCategoryData(false)
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


    private suspend fun processTransactions(transactions: List<Category>, isRevenue: Boolean) {
        val categorySums = transactions.groupBy { it.id }.map { entry ->
            val categoryName = categoryRepository.getCategory(entry.key).nom
            val sum = entry.value.sumOf { it.solde }
            Category(entry.key, categoryName, sum)
        }
        if (isRevenue) {
            _revenueData.postValue(categorySums)
        } else {
            _expenseData.postValue(categorySums)
        }
    }


    private suspend fun getCategoryNameById(categoryId: Int): String {
        Log.d("HomeViewModel", "Fetched category: ${categoryRepository.getCategory(categoryId).nom}")
        return categoryRepository.getCategory(categoryId).nom

    }
}
