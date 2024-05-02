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
}
