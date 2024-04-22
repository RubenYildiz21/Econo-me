package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.helmo.projetmobile.databinding.ListItemAccountBinding
import be.helmo.projetmobile.databinding.ListItemCategoryBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import com.bumptech.glide.Glide
import java.util.UUID

/**
 * Utilise list_item_category comme model pour afficher les categories en bd grace a ce model
 */
class AccountListAdapter(val account: List<Compte>, private val onAccountClicked: (accountId: Int) -> Unit) : RecyclerView.Adapter<AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(binding)
    }

    override fun getItemCount(): Int {
        return account.size
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(account[position], onAccountClicked)
    }
}

class AccountHolder(val binding: ListItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(account: Compte, onCategoryClicked: (accountId: Int) -> Unit) {
        binding.textViewAccountName.text = account.nom
        binding.textViewAccountBalance.text = "${account.solde} ${account.devise}"
        binding.root.setOnClickListener {
            onCategoryClicked(account.id)
        }
    }
}
