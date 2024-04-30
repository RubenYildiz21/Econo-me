package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.databinding.FragmentTransactionAddBinding
import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import be.helmo.projetmobile.viewmodel.TransactionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TransactionDialogFragment: BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var dialogName: TextView
    private var transaction: Transaction? = null
    private val viewModel: TransactionListViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository.get(),
            ViewModelProvider(requireActivity()).get(AccountListViewModel::class.java),
            ViewModelProvider(requireActivity()).get(CategoryListViewModel::class.java)
        )
    }
    private var _binding: FragmentTransactionAddBinding? = null

    private val categoryListViewModel: CategoryListViewModel by viewModels()
    private val accountListViewModel: AccountListViewModel by viewModels()

    private lateinit var binding: FragmentTransactionAddBinding

    private var mode: Mode = Mode.CREATE

    companion object {
        fun newInstance(transaction: Transaction?, mode: Mode): TransactionDialogFragment {
            val fragment = TransactionDialogFragment()
            fragment.transaction = transaction
            fragment.mode = mode
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)

        loadCategories()
        loadAccounts()

        confirmButton.setOnClickListener {
            submitData()
        }
    }

    fun loadAccounts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountListViewModel.account.collect { account ->
                    if (account.isNotEmpty()) {
                        val name = account.map { "${it.nom} (${it.devise})"}
                        updateAccountDropdown(name)
                    }
                }
            }
        }
    }

    private fun updateAccountDropdown(name: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, name)
        binding.accountSpinner.setAdapter(adapter)
    }

    fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryListViewModel.categories.collect { categories ->
                    val name = categories.map { "${it.nom}"}
                    updateCategoriesDropdown(name)
                }
            }
        }
    }

    private fun updateCategoriesDropdown(name: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, name)
        binding.categorySpinner.setAdapter(adapter)
    }

    private fun setupViews(view: View) {
        confirmButton = view.findViewById(R.id.AddTransaction)
        nameEditText = view.findViewById(R.id.transactionName)
        dialogName = view.findViewById(R.id.dialogName)

        // Mettre à jour le texte du bouton en fonction du mode
        if (mode == Mode.EDIT) {
            confirmButton.text = "Modifier"
            dialogName.text = "Modifier la transaction"
        }
        // Charger les données existantes si en mode 'Modifier'
        transaction?.let {
            nameEditText.setText(it.nom)
        }
    }


    private fun submitData() {
        val name = nameEditText.text.toString()
        if (name == "") {
            Toast.makeText(context, "Veuillez entrer un nom pour la transaction.", Toast.LENGTH_LONG).show()
            return
        }
        val category = binding.categorySpinner.text.toString()
        if (category == "") {
            Toast.makeText(context, "Veuillez selctionner une categorie.", Toast.LENGTH_LONG).show()
            return
        }
        val compte = binding.accountSpinner.text.toString().split(" (")[0]
        if (compte == "") {
            Toast.makeText(context, "Veuillez selctionner un compte.", Toast.LENGTH_LONG).show()
            return
        }
        val date = getDate()
        val montantString = binding.transactionPrice.text.toString()
        if (montantString == "") {
            Toast.makeText(context, "Veuillez entrer un montant valide.", Toast.LENGTH_LONG).show()
            return
        }
        val montant: Double = montantString.toDoubleOrNull() ?: 0.0
        var type = true
        if (montant >= 0) {
            type = true
        } else {
            type = false
        }

        val updatedTransaction = Transaction(transaction?.id ?: 0, name, 0, 0, date, montant, "", "", type)
        //transactionViewModel.saveOrUpdateTransaction(updatedTransaction)
        viewModel.createTransaction(compte, category, montant, updatedTransaction)
        viewModel.onTransactionComplete = {
            dismiss()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDate(): Date {
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }
}