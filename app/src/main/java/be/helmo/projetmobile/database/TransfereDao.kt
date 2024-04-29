package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.Transfere
import be.helmo.projetmobile.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TransfereDao {
    @Query("SELECT * FROM TRANSFERE")
    fun getTransferes(): Flow<List<Transfere>>

    @Query("SELECT * FROM TRANSFERE WHERE id=(:transfereId)")
    suspend fun getTransfere(transfereId: Int): Transfere

    @Insert
    suspend fun addTransfere(transfere: Transfere)

    @Update
    suspend fun updateTransfere(transfere: Transfere)
}