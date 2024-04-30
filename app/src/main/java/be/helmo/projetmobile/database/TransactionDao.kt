package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TransactionDao {

    @Query("SELECT * FROM 'TRANSACTION'")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'TRANSACTION' WHERE id=(:transactionId)")
    suspend fun getTransaction(transactionId: UUID): Transaction

    @Insert
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM 'TRANSACTION' WHERE strftime('%m', date) = :month")
    fun getTransactionsByMonth(month: String): Flow<List<Transaction>>
}