package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class CurrencyViewModel : ViewModel() {
    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> = _currencies

    private val exchangeService: ExchangeServices by lazy {
        Retrofit.Builder()
            .baseUrl("http://api.exchangeratesapi.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeServices::class.java)
    }

    fun fetchCurrencies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = exchangeService.getLatestRates(apiKey)
                if (response.isSuccessful && response.body() != null) {
                    val rates = response.body()!!.rates
                    val currencyList = rates.map { Currency(it.key, it.value.toString()) }
                    _currencies.postValue(currencyList)
                    Log.d("CurrencyFetch", "Currencies fetched successfully: ${_currencies.value}")
                } else {
                    Log.e("CurrencyFetch", "Failed to fetch currencies: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CurrencyFetch", "Exception during currency fetch: ${e.message}")
            }
        }
    }

    suspend fun getExchangeRate(sourceCurrency: String, destinationCurrency: String): Double {
        return try {
            val response = exchangeService.getLatestRates("68bf113bb26cb8b6af82d7c07b8be2b3") // Replace "your_access_key" with actual API key
            response.body()?.rates?.get(destinationCurrency) ?: 1.0
        } catch (e: Exception) {
            Log.e("CurrencyViewModel", "Error fetching exchange rate: ${e.message}")
            1.0  // Return a default or error code
        }
    }

    interface ExchangeServices {
        @GET("latest")
        suspend fun getLatestRates(@Query("access_key") apiKey: String): retrofit2.Response<CurrencyResponse>
    }
}

data class CurrencyResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

data class Currency(val code: String, val name: String)
