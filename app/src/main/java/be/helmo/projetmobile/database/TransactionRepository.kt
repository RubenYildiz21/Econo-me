package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class TransactionRepository (val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getTransactions(): Flow<List<Transaction>> = database.transactionDao().getTransactions()

    suspend fun getTransaction(transactionId: UUID): Transaction = database.transactionDao().getTransaction(transactionId)

    suspend fun addTransaction(transaction: Transaction) = database.transactionDao().addTransaction(transaction)

    fun updateTransaction(transaction: Transaction) {
        coroutineScope.launch {
            database.transactionDao().updateTransaction(transaction)
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