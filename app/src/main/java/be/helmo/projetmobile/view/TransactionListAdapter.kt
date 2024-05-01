package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import be.helmo.placereport.view.getScaledBitmap
import be.helmo.projetmobile.R
import be.helmo.projetmobile.databinding.ListItemAccountBinding
import be.helmo.projetmobile.databinding.ListItemTransactionBinding
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class TransactionListAdapter(
    val transaction: List<Transaction>,
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


    inner class TransactionHolder(val binding: ListItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction, onEditClicked: (transactionId: Int) -> Unit, onDeleteClicked: (transactionId: Int) -> Unit) {
            binding.transactionName.text = transaction.nom
            binding.transactionPrice.text = "${transaction.solde}"
            binding.categoryName.text = transaction.categoryId.toString()
            if (transaction.facture != "") {
                updatePhoto(binding.showFacture, transaction.facture)
            }
            binding.compteName.text = transaction.compteId.toString()
            binding.editAccount.setOnClickListener {
                onEditClicked(transaction.id)
            }
            binding.deleteAccount.setOnClickListener{
                onDeleteClicked(transaction.id)
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