package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.databinding.ListItemAccountBinding
import be.helmo.projetmobile.model.Compte

/**
 * Utilise list_item_category comme model pour afficher les categories en bd grace a ce model
 */
class AccountListAdapter(
    val account: List<Compte>,
    private val onAccountClicked: (accountId: Int) -> Unit,
    private val onEditClicked: (accountId: Int) -> Unit // nouveau paramètre
) : RecyclerView.Adapter<AccountListAdapter.AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(binding)
    }

    override fun getItemCount(): Int {
        return account.size
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(account[position], onAccountClicked, onEditClicked)
    }


    inner class AccountHolder(val binding: ListItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Compte, onAccountClicked: (accountId: Int) -> Unit, onEditClicked: (accountId: Int) -> Unit) {
            binding.textViewAccountName.text = account.nom
            binding.textViewAccountBalance.text = "${account.solde} ${account.devise}"
            binding.root.setOnClickListener {
                onAccountClicked(account.id)
            }
            binding.editAccount.setOnClickListener { // gestionnaire de clics pour le bouton "editAccount"
                onEditClicked(account.id)
            }
        }
    }
}


