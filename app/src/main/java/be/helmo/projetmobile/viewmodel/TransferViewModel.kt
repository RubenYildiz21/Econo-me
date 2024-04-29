package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.TransfereRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TransferViewModel(
    private val transfereRepository: TransfereRepository,
    private val currencyViewModel: CurrencyViewModel,
    private val accountViewModel: AccountListViewModel
) : ViewModel() {

    fun performTransfer(sourceAccountName: String, destinationAccountName: String, amount: Double) {
        viewModelScope.launch {
            // Assurez-vous que les comptes sont chargés
            accountViewModel.accounts.collect { accounts ->
                if (accounts.isEmpty()) {
                    Log.d("Transfer", "Pas de compte disponible pour effectuer un transfert.")
                }

                val sourceAccount = accounts.firstOrNull { it.nom == sourceAccountName }
                val destinationAccount = accounts.firstOrNull { it.nom == destinationAccountName }

                if (sourceAccount == null || destinationAccount == null) {
                    Log.d("Transfer", "Un des comptes est inexistant.")
                    return@collect
                }

                if (sourceAccount.solde < amount) {
                    Log.d("Transfer", "Fonds insuffisants.")
                    return@collect
                }

                val rate = if (sourceAccount.devise != destinationAccount.devise) {
                    getExchangeRate(sourceAccount.devise, destinationAccount.devise)
                } else 1.0

                transfereRepository.transferAmount(sourceAccount.id, destinationAccount.id, amount, rate)
            }
        }
    }


    // Assume this method exists and correctly fetches exchange rates
    suspend fun getExchangeRate(sourceCurrency: String, destinationCurrency: String): Double {
        return currencyViewModel.getExchangeRate(sourceCurrency, destinationCurrency)
    }
}
