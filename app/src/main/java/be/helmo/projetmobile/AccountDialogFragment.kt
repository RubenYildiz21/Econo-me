package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccountDialogFragment : BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var currencyEditText: EditText
    private lateinit var balanceEditText: EditText
    private var account: Compte? = null  // Compte pour la modification
    private val accountViewModel: AccountListViewModel by viewModels()

    companion object {
        fun newInstance(account: Compte?): AccountDialogFragment {
            val fragment = AccountDialogFragment()
            fragment.account = account
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        confirmButton.setOnClickListener {
            submitData()
        }
    }

    private fun setupViews(view: View) {
        confirmButton = view.findViewById(R.id.addAccount)
        nameEditText = view.findViewById(R.id.accountName)
        currencyEditText = view.findViewById(R.id.accountCurrency)
        balanceEditText = view.findViewById(R.id.accountBalance)

        // Charger les données existantes si en mode 'Modifier'
        account?.let {
            nameEditText.setText(it.nom)
            currencyEditText.setText(it.devise)
            balanceEditText.setText(it.solde.toString())
        }
    }

    private fun submitData() {
        val name = nameEditText.text.toString()
        val currency = currencyEditText.text.toString()
        val balance = balanceEditText.text.toString().toDouble()
        val updatedAccount = Compte(account?.id ?: 0, name, currency, balance)
        // Appeler ViewModel pour sauvegarder ou mettre à jour
        accountViewModel.saveOrUpdateAccount(updatedAccount)
        dismiss()
    }
}
