package be.helmo.projetmobile.viewmodel

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.CompteRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.time.Month
import java.util.Calendar
import java.util.Date

class TransactionListViewModel(
    private val transactionRepository: TransactionRepository,
    private val accountViewModel: AccountListViewModel,
    private val categoryViewModel: CategoryListViewModel
) : ViewModel() {

    private val _transaction: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())
    val transaction : StateFlow<List<Transaction>>
        get() = _transaction.asStateFlow()

    var transac: Transaction? = null

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionRepository.getTransactions().collect { listOfTransac ->
                _transaction.value = listOfTransac
            }
        }
    }

    fun loadTransactionsByMonth(month: Int) {
        val filteredTransactions = _transaction.value.filter { transaction ->
            val cal = Calendar.getInstance()
            cal.time = transaction.date
            cal.get(Calendar.MONTH) == month
        }
        _transaction.value = filteredTransactions
    }

    fun updateLieu(transactionId: Int, pos: LatLng) {
        viewModelScope.launch {
            val list =transaction.first()
            val t = list.firstOrNull {it.id == transactionId}
            if (t != null) {
                t.lieu = pos
                transactionRepository.updateTransaction(t)
            }
        }
    }

    fun saveOrUpdateTransaction(compte: String, category: String, montant: Double, transaction: Transaction) {
        viewModelScope.launch {
            if (transaction.id > 0) {
                // L'ID existe, donc c'est une mise à jour
                val newT = createTransaction(compte, category, montant, transaction)
                if (newT != null) {
                    transactionRepository.updateTransaction(newT)
                    onTransactionComplete?.invoke()
                }
            } else {
                val newT = createTransaction(compte, category, montant, transaction)
                if (newT != null) {
                    transactionRepository.addTransaction(newT)
                    onTransactionComplete?.invoke()
                }
            }
        }
    }


    var errorMessage = MutableLiveData<String>()
    var onTransactionComplete: (() -> Unit)? = null
    suspend fun createTransaction(accountName: String, categoryName: String, montant: Double, transaction: Transaction): Transaction? {
        var newTransaction: Transaction? = null
        viewModelScope.launch {
            val accounts = accountViewModel.accounts.first()
            val acc = accounts.firstOrNull { it.nom == accountName}

            val category = categoryViewModel.categories.first()
            val cat = category.firstOrNull { it.nom == categoryName }

            if (acc != null) {
                Log.d("TransactionListViewModel", "Type de transaction : ${transaction.type}")
                if (!transaction.type){
                    if (acc.solde < montant) {
                        errorMessage.postValue("Le solde disponible sur le compte ${acc.nom} n'est pas suffisant")
                        return@launch
                    }
                }
            }

            newTransaction = Transaction(transaction.id ?: 0, transaction.nom, cat!!.id, acc!!.id, transaction.date, montant, LatLng(0.0, 0.0), transaction.devise, transaction.facture, transaction.type)
        }.join()
        return newTransaction
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
            loadTransactions()
        }
    }

    suspend fun getAllRevenu(): Double {
        var totalRevenu = 0.0

        viewModelScope.launch {
            transaction.collect { transactions ->
                for (transaction in transactions) {
                    if (transaction.type) {
                        totalRevenu += transaction.solde
                    }
                }
            }
        }.join()
        return totalRevenu
    }

    suspend fun getAllDepense(): Double {
        var totalDepense = 0.0

        viewModelScope.launch {
            transaction.collect { transactions ->
                for (transaction in transactions) {
                    if (!transaction.type) {
                        totalDepense += transaction.solde
                    }
                }
            }
        }.join()
        return totalDepense
    }

    fun getPos(id: Int): LatLng {
        var pos: LatLng = LatLng(0.0, 0.0)
        viewModelScope.launch {
            val list =transaction.first()
            val t = list.firstOrNull {it.id == id}
            if (t != null) {
                pos = t.lieu
            }
        }
        return pos
    }

    fun getImage(id: Int): String {
        var nom: String = ""
        viewModelScope.launch {
            val list = transaction.first()
            val t = list.firstOrNull {it.id == id}
            if (t != null) {
                nom = t.facture
            }
        }
        return nom
    }

    fun getTransactioByID(id: Int): Transaction? {
        var trans: Transaction? = null
        viewModelScope.launch {
            val list = transaction.first()
            trans = list.firstOrNull { it.id == id }
        }
        return trans
    }
}