package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.model.Transfere
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class TransfereRepository (val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getTransferes(): Flow<List<Transfere>> = database.transferDao().getTransferes()

    suspend fun getTransfere(transfereId: UUID): Transfere = database.transferDao().getTransfere(transfereId)

    suspend fun addTransfere(transfere: Transfere) = database.transferDao().addTransfere(transfere)

    fun updateTransfere(transfere: Transfere) {
        coroutineScope.launch {
            database.transferDao().updateTransfere(transfere)
        }
    }

    companion object {
        private var INSTANCE: TransfereRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = TransfereRepository(database)
        }

        fun get(): TransfereRepository {
            if(INSTANCE == null)
                INSTANCE = TransfereRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}