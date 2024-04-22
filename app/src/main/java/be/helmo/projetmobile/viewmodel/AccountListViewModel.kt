package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.model.Compte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountListViewModel : ViewModel() {

    private val accountRepository: CompteRepository = CompteRepository.get()

    private val _account: MutableStateFlow<List<Compte>> = MutableStateFlow(emptyList())
    val account : StateFlow<List<Compte>>
        get() = _account.asStateFlow()

    init {
        loadComptes()
    }

    private fun loadComptes() {
        viewModelScope.launch {
            accountRepository.getComptes().collect { listOfComptes ->
                _account.value = listOfComptes
            }
        }
    }

    suspend fun addCompte(name: String, currency: String, balance: Double) : Int {
        val account = Compte(0, name, currency, balance)
        accountRepository.addCompte(account)
        return account.id
    }
}