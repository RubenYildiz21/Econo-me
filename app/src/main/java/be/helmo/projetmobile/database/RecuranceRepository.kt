package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Recurance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class RecuranceRepository (val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getRecurances(): Flow<List<Recurance>> = database.recuranceDao().getRecurances()

    suspend fun getRecurance(recuranceID: UUID): Recurance = database.recuranceDao().getRecurance(recuranceID)

    suspend fun addRecurance(recurance: Recurance) = database.recuranceDao().addRecurance(recurance)

    fun updateRecurance(recurance: Recurance) {
        coroutineScope.launch {
            database.recuranceDao().updateRecurance(recurance)
        }
    }

    companion object {
        private var INSTANCE: RecuranceRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = RecuranceRepository(database)
        }

        fun get(): RecuranceRepository {
            if(INSTANCE == null)
                INSTANCE = RecuranceRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}