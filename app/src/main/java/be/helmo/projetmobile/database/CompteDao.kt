package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.Compte
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CompteDao {

    @Query("SELECT * FROM COMPTE")
    fun getComptes(): Flow<List<Compte>>

    @Query("SELECT * FROM COMPTE WHERE id=(:compteId)")
    suspend fun getCompte(compteId: UUID): Compte

    @Insert
    suspend fun addCompte(compte: Compte)

    @Update
    suspend fun updateCompte(compte: Compte)
}