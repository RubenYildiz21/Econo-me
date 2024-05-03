package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.databinding.FragmentAccountAddBinding
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccountDialogFragment : BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var currencyEditText: EditText
    private lateinit var balanceEditText: EditText
    private lateinit var dialogName: TextView
    private var account: Compte? = null
    private val accountViewModel: AccountListViewModel by viewModels()
    private val currencyViewModel: CurrencyViewModel by viewModels()
    private var _binding: FragmentAccountAddBinding? = null
    private val binding get() = _binding!!

    private var mode: Mode = Mode.CREATE

    companion object {
        fun newInstance(account: Compte?, mode: Mode): AccountDialogFragment {
            val fragment = AccountDialogFragment()
            fragment.account = account
            fragment.mode = mode
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupCurrencyDropdown()
        confirmButton.setOnClickListener {
            submitData()
        }
    }

    private fun setupViews(view: View) {
        confirmButton = view.findViewById(R.id.addAccount)
        nameEditText = view.findViewById(R.id.accountName)
        currencyEditText = view.findViewById(R.id.accountCurrency)
        balanceEditText = view.findViewById(R.id.accountBalance)
        dialogName = view.findViewById(R.id.dialogName)

        // Mettre à jour le texte du bouton en fonction du mode
        if (mode == Mode.EDIT) {
            confirmButton.text = "Modifier"
            dialogName.text = "Modifier le compte"
        }
        // Charger les données existantes si en mode 'Modifier'
        account?.let {
            nameEditText.setText(it.nom)
            currencyEditText.setText(it.devise)
            balanceEditText.setText(it.solde.toString())
        }
    }

    private fun setupCurrencyDropdown() {

        currencyViewModel.currencies.observe(viewLifecycleOwner) { currencies ->
            Log.d("CurrencyDropdown", "Updating currency dropdown with new data: $currencies")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currencies.map { it.code })
            binding.accountCurrency.setAdapter(adapter)
        }
        currencyViewModel.fetchCurrencies("8ff3d33aabc635aadd0974baa8774935")
    }


    private fun submitData() {
        val name = nameEditText.text.toString()
        val currency = currencyEditText.text.toString()
        val balance = balanceEditText.text.toString().toDouble()
        val updatedAccount = Compte(account?.id ?: 0, name, currency, balance)
        accountViewModel.saveOrUpdateAccount(updatedAccount)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
