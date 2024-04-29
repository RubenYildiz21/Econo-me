package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.model.Compte
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AccountListViewModel : ViewModel() {

    private val accountRepository: CompteRepository = CompteRepository.get()

    private val _accounts: MutableStateFlow<List<Compte>> = MutableStateFlow(emptyList())

    val accounts: StateFlow<List<Compte>> = _accounts.asStateFlow()
    val account : StateFlow<List<Compte>>
        get() = _accounts.asStateFlow()

    init {
        loadComptes()
    }

    private fun loadComptes() {
        viewModelScope.launch {
            accountRepository.getComptes().collect { listOfComptes ->
                _accounts.value = listOfComptes
            }
        }
    }

    fun saveOrUpdateAccount(account: Compte) {
        viewModelScope.launch {
            if (account.id > 0) {
                // L'ID existe, donc c'est une mise à jour
                accountRepository.updateCompte(account)
            } else {
                // Pas d'ID, donc c'est une nouvelle entrée
                accountRepository.addCompte(account)
            }
        }
    }

    fun deleteAccount(account: Compte) {
        viewModelScope.launch {
            accountRepository.deleteCompte(account)
            loadComptes()  // Refresh the list after deletion
        }
    }
}