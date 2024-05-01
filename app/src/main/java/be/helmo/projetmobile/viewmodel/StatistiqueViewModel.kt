package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.model.Category
import kotlinx.coroutines.launch
import okhttp3.internal.format
import java.time.LocalDate

class StatistiqueViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    private val _categoriesMonth = MutableLiveData<List<Category>>()
    val categoriesMonth: LiveData<List<Category>> = _categoriesMonth

    private val _categoriesYear = MutableLiveData<List<Category>>()
    val categoriesYear: LiveData<List<Category>> = _categoriesYear

    fun loadCategoriesForCurrentMonth() {
        val currentMonth = LocalDate.now().monthValue.toString().padStart(2, '0')
        Log.d("StatViewModel", "Current Month : ${currentMonth}")
        viewModelScope.launch {
            categoryRepository.getCategoriesByMonth(currentMonth).collect { categories ->
                _categoriesMonth.postValue(categories)
            }
        }
    }

    fun loadCategoriesForCurrentYear() {
        val currentYear = LocalDate.now().year.toString()
        Log.d("StatViewModel", "Current Year : ${currentYear}")
        viewModelScope.launch {
            categoryRepository.getCategoriesByYear(currentYear).collect { categories ->
                _categoriesYear.postValue(categories)
            }
        }
    }
}
