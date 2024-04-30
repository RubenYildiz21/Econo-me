package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
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
        val updateAmountCompte = account.copy(solde = account.solde + transaction.solde)

        val category = database.categoryDao().getCategory(transaction.categoryId)
        val updateAmountCategory = category.copy(solde = category.solde - transaction.solde)

        coroutineScope.launch {
            database.compteDao().updateCompte(updateAmountCompte)
            database.categoryDao().updateCategory(updateAmountCategory)
            database.transactionDao().addTransaction(transaction)
        }
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