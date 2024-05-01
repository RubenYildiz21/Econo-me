package be.helmo.projetmobile.database

import android.util.Log
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Month
import java.util.UUID

class TransactionRepository (val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getTransactions(): Flow<List<Transaction>> = database.transactionDao().getTransactions()

    fun getTransactionsByMonth(month: String): Flow<List<Transaction>> = database.transactionDao().getTransactionsByMonth(month)

    suspend fun getTransaction(transactionId: UUID): Transaction = database.transactionDao().getTransaction(transactionId)

    suspend fun addTransaction(transaction: Transaction) {
        val account = database.compteDao().getCompte(transaction.compteId)
        var updateAmountCompte: Compte
        if (transaction.type){
            updateAmountCompte = account.copy(solde = account.solde + transaction.solde)
        }else{
            updateAmountCompte = account.copy(solde = account.solde - transaction.solde)
        }

        val rate = if (account.devise != "EUR") {
            try {
                CurrencyViewModel.getRate(account.devise)
            } catch (e: Exception) {
                Log.e("TransferViewModel", "Error fetching exchange rate: ${e.message}")
                1.0  // Fallback rate in case of failure
            }
        } else {
            1.0
        }

        val finalAmount = transaction.solde / rate
        val category = database.categoryDao().getCategory(transaction.categoryId)
        val updateAmountCategory = category.copy(solde = category.solde + finalAmount)

        coroutineScope.launch {
            database.compteDao().updateCompte(updateAmountCompte)
            database.categoryDao().updateCategory(updateAmountCategory)
            database.transactionDao().addTransaction(transaction)
        }.join()
    }

    fun updateTransaction(transaction: Transaction) {
        coroutineScope.launch {
            database.transactionDao().updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        coroutineScope.launch {
            database.transactionDao().deleteTransaction(transaction)
        }
    }

    companion object {
        private var INSTANCE: TransactionRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = TransactionRepository(database)
        }

        fun get(): TransactionRepository {
            if(INSTANCE == null)
                INSTANCE = TransactionRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}