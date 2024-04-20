package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.Recurance
import be.helmo.projetmobile.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface RecuranceDao {
    @Query("SELECT * FROM RECURANCE")
    fun getRecurances(): Flow<List<Recurance>>

    @Query("SELECT * FROM RECURANCE WHERE id=(:recuranceId)")
    suspend fun getRecurance(recuranceId: UUID): Recurance

    @Insert
    suspend fun addRecurance(recurance: Recurance)

    @Update
    suspend fun updateRecurance(recurance: Recurance)
}
