package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.helmo.projetmobile.databinding.ListItemTransactionBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction

class TransactionListAdapter(
    val transaction: List<Transaction>,
    private val accounts: List<Compte>,
    private val categories: List<Category>,
    private val onEditClicked: (transactionId: Int) -> Unit,
    private val onDeleteClicked: (transactionId: Int) -> Unit
) : RecyclerView.Adapter<TransactionListAdapter.TransactionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTransactionBinding.inflate(inflater, parent, false)
        return TransactionHolder(binding)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        holder.bind(transaction[position], onEditClicked, onDeleteClicked)
    }

    private fun getAccountNameById(id: Int): String {
        return accounts.find { it.id == id }?.nom ?: "Unknown Account"
    }

    private fun getCategoryNameById(id: Int): String {
        return categories.find { it.id == id }?.nom ?: "Unknown Category"
    }

    inner class TransactionHolder(val binding: ListItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction, onEditClicked: (transactionId: Int) -> Unit, onDeleteClicked: (transactionId: Int) -> Unit) {
            binding.transactionName.text = transaction.nom
            binding.transactionPrice.text = String.format("%.2f", transaction.solde)
            binding.categoryName.text = getCategoryNameById(transaction.categoryId)
            binding.compteName.text = getAccountNameById(transaction.compteId)
            binding.transactionCurrency.text = transaction.devise
            binding.editAccount.setOnClickListener {
                onEditClicked(transaction.id)
            }
            binding.deleteAccount.setOnClickListener{
                onDeleteClicked(transaction.id)
            }
        }
    }
}