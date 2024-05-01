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
            val response = exchangeService.getLatestRates("68bf113bb26cb8b6af82d7c07b8be2b3")
            val rates = response.body()?.rates ?: return 1.0

            // Gérer le cas où l'euro est impliqué
            if (sourceCurrency == "EUR") {
                return rates[destinationCurrency] ?: 1.0
            } else if (destinationCurrency == "EUR") {
                val rate = rates[sourceCurrency] ?: return 1.0
                return 1 / rate // Inverser le taux si on convertit vers l'euro
            } else {
                // Autres devises (ni source ni destination ne sont l'euro)
                val rateToEUR = rates[sourceCurrency] ?: return 1.0
                val rateFromEURToDest = rates[destinationCurrency] ?: return 1.0
                return rateFromEURToDest / rateToEUR // Convertir via EUR si nécessaire
            }
        } catch (e: Exception) {
            Log.e("CurrencyViewModel", "Error fetching exchange rate: ${e.message}")
            1.0  // Return a default or error code
        }
    }


    interface ExchangeServices {
        @GET("latest")
        suspend fun getLatestRates(@Query("access_key") apiKey: String): retrofit2.Response<CurrencyResponse>
    }

    companion object {
        private val exchangeService: ExchangeServices by lazy {
            Retrofit.Builder()
                .baseUrl("http://api.exchangeratesapi.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExchangeServices::class.java)
        }
        suspend fun getRate(currency: String): Double {
            return try {
                val response = exchangeService.getLatestRates("68bf113bb26cb8b6af82d7c07b8be2b3")
                response.body()?.rates?.get(currency) ?: 1.0
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Error fetching rate for $currency: ${e.message}")
                1.0
            }
        }
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
