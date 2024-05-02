package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    var onTransferCompleted: (() -> Unit)? = null
    val errorMessage = MutableLiveData<String>()
    fun performTransfer(sourceAccountName: String, destinationAccountName: String, amount: Double) {
        viewModelScope.launch {
            Log.d("Transfer", "Attempting to transfer $amount from $sourceAccountName to $destinationAccountName")
            val accounts = accountViewModel.accounts.first() // Prend l'état actuel des comptes
            val sourceAccount = accounts.firstOrNull { it.nom == sourceAccountName }
            val destinationAccount = accounts.firstOrNull { it.nom == destinationAccountName }

            if (sourceAccount == null || destinationAccount == null) {
                Log.d("Transfer", "Un des comptes est inexistant.")
                errorMessage.postValue("L'un des comptes spécifiés n'existe pas.")
                return@launch
            }

            if (sourceAccount.solde < amount) {
                Log.d("Transfer", "Fonds insuffisants.")
                errorMessage.postValue("Fonds insuffisants pour réaliser le transfert.")
                return@launch
            }

            val sourceCurrencyCode = sourceAccount.devise.split(" - ")[0]
            val destinationCurrencyCode = destinationAccount.devise.split(" - ")[0]

            val rate = if (sourceAccount.devise != destinationAccount.devise) {
                try {
                    Log.d("TransferViewModel", "Source currency : ${sourceAccount.devise} --- Destination currency : ${destinationAccount.devise}")
                    currencyViewModel.getExchangeRate(sourceCurrencyCode, destinationCurrencyCode)
                } catch (e: Exception) {
                    Log.e("TransferViewModel", "Error fetching exchange rate: ${e.message}")
                    1.0  // taux par defaut en cas d'erreur
                }
            } else {
                1.0
            }


            transfereRepository.transferAmount(sourceAccount.id, destinationAccount.id, amount, rate)
            onTransferCompleted?.invoke()


            Log.d("Transfer", "Transfer completed: $amount from ${sourceAccount.nom} to ${destinationAccount.nom} at rate $rate")
        }
    }

}
