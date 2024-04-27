package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Compte
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class CompteRepository private constructor(val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getComptes(): Flow<List<Compte>> = database.compteDao().getComptes()

    suspend fun getCompte(compteId: UUID): Compte = database.compteDao().getCompte(compteId)

    suspend fun addCompte(compte: Compte) = database.compteDao().addCompte(compte)

    fun updateCompte(compte: Compte) {
        coroutineScope.launch {
            database.compteDao().updateCompte(compte)
        }
    }

    fun deleteCompte(compte: Compte) {
        coroutineScope.launch {
            database.compteDao().deleteCompte(compte)
        }
    }

    companion object {
        private var INSTANCE: CompteRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = CompteRepository(database)
        }

        fun get(): CompteRepository {
            if(INSTANCE == null)
                INSTANCE = CompteRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}