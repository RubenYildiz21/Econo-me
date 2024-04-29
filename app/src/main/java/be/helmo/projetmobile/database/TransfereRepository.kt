package be.helmo.projetmobile.database

import android.util.Log
import be.helmo.projetmobile.model.Transfere
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class TransfereRepository (val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getTransferes(): Flow<List<Transfere>> = database.transferDao().getTransferes()

    suspend fun getTransfere(transfereId: Int): Transfere = database.transferDao().getTransfere(transfereId)

    suspend fun addTransfere(transfere: Transfere) = database.transferDao().addTransfere(transfere)

    fun updateTransfere(transfere: Transfere) {
        coroutineScope.launch {
            database.transferDao().updateTransfere(transfere)
        }
    }

    suspend fun transferAmount(sourceAccountId: Int, destinationAccountId: Int, amount: Double, exchangeRate: Double) {
        val sourceAccount = database.compteDao().getCompte(sourceAccountId)
        val destinationAccount = database.compteDao().getCompte(destinationAccountId)

        if (sourceAccount == null || destinationAccount == null) {
            Log.e("TransferError", "Un des compte n'existe pas.")
            return
        }

        if (sourceAccount.solde < amount) {
            Log.e("TransferError", "Fonds insuffisant")
            return
        }

        // Apply exchange rate only if currencies differ
        val convertedAmount = if (sourceAccount.devise != destinationAccount.devise) {
            amount * exchangeRate
        } else {
            amount
        }

        // Create new instances with updated balances
        val updatedSourceAccount = sourceAccount.copy(solde = sourceAccount.solde - amount)
        val updatedDestinationAccount = destinationAccount.copy(solde = destinationAccount.solde + convertedAmount)

        coroutineScope.launch {
            database.compteDao().updateCompte(updatedSourceAccount)
            database.compteDao().updateCompte(updatedDestinationAccount)
            val transfere = Transfere(0, source = sourceAccountId, destination = destinationAccountId, montant = amount)
            database.transferDao().addTransfere(transfere)
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