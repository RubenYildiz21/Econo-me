package be.helmo.projetmobile.view

import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import be.helmo.placereport.view.getScaledBitmap
import be.helmo.projetmobile.FragmentDetailTransaction
import be.helmo.projetmobile.R
import be.helmo.projetmobile.databinding.ListItemAccountBinding
import be.helmo.projetmobile.databinding.ListItemTransactionBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import java.io.File

class TransactionListAdapter(
    val transaction: List<Transaction>,
    private val accounts: List<Compte>,
    private val categories: List<Category>,
    private val onEditClicked: (transactionId: Int) -> Unit,
    private val onDeleteClicked: (transactionId: Int) -> Unit,
    private val showMapFragment: (transitionId: Int) -> Unit,
    private val onTransactionClicked: (transitionId: Int) -> Unit
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
        holder.bind(transaction[position], onEditClicked, onDeleteClicked, showMapFragment, onTransactionClicked)
    }

    private fun getAccountNameById(id: Int): String {
        return accounts.find { it.id == id }?.nom ?: "Unknown Account"
    }

    private fun getCategoryNameById(id: Int): String {
        return categories.find { it.id == id }?.nom ?: "Unknown Category"
    }

    inner class TransactionHolder(val binding: ListItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction, onEditClicked: (transactionId: Int) -> Unit, onDeleteClicked: (transactionId: Int) -> Unit, showMapFragment: (transactionId: Int) -> Unit, onTransactionClicked: (transactionId: Int) -> Unit) {
            binding.transactionName.text = transaction.nom
            binding.transactionPrice.text = "${transaction.solde}"
            binding.categoryName.text = transaction.categoryId.toString()
            binding.compteName.text = transaction.compteId.toString()
            binding.transactionPrice.text = String.format("%.2f", transaction.solde)
            if (transaction.type) {
                val color = ContextCompat.getColor(itemView.context, R.color.green)
                binding.transactionPrice.setTextColor(color)
            } else {
                val color = ContextCompat.getColor(itemView.context, R.color.red)
                binding.transactionPrice.setTextColor(color)
            }
            binding.categoryName.text = getCategoryNameById(transaction.categoryId)
            binding.compteName.text = getAccountNameById(transaction.compteId)
            binding.transactionCurrency.text = transaction.devise
            binding.editAccount.setOnClickListener {
                onEditClicked(transaction.id)
            }
            binding.deleteAccount.setOnClickListener{
                onDeleteClicked(transaction.id)
            }
            binding.showFacture.setOnClickListener {
                showMapFragment(transaction.id)
            }
            binding.transaction.setOnClickListener {
                onTransactionClicked(transaction.id)
            }
        }

        private fun updatePhoto(facturePhoto: ImageView, photoFileName: String) {
            val photoFile = File(facturePhoto.context.applicationContext.filesDir, photoFileName)
            if (photoFile.exists()) {
                facturePhoto.doOnLayout {
                    val photo = getScaledBitmap(photoFile.path, it.width, it.height)
                    facturePhoto.setImageBitmap(photo)
                }
            }
        }
    }
}