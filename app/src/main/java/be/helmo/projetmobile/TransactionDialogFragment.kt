package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.databinding.FragmentAccountAddBinding
import be.helmo.projetmobile.databinding.FragmentCategoryListBinding
import be.helmo.projetmobile.databinding.FragmentTransactionAddBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.view.CategoryListAdapter
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TransactionDialogFragment: BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var categoryEdit: EditText
    private lateinit var dateEdit: EditText
    private lateinit var montantEdit: EditText
    private lateinit var lieuEdit: EditText
    private lateinit var dialogName: TextView
    private var transaction: Transaction? = null
    private val transactionViewModel: TransactionListViewModel by viewModels()
    private var _binding: FragmentTransactionAddBinding? = null

    private val categoryListViewModel: CategoryListViewModel by viewModels()
    private val accountListViewModel: AccountListViewModel by viewModels()
    //private val binding get() = _binding!!
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

        //changeDate()
        loadCategories()
        loadAccounts()

        confirmButton.setOnClickListener {
            submitData()
        }
    }

    fun loadAccounts() {
        val spinner: Spinner = binding.accountSpinner

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountListViewModel.account.collect { account ->
                    val adapter = createAdapterAccount(account)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            }
        }
    }

    fun loadCategories() {
        //val spinner: Spinner = binding.categorySpinner

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryListViewModel.categories.collect { categories ->
                    //val adapter = createAdapter(categories)
                    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    //spinner.adapter = adapter
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories.map { "${it.nom}" })
                    binding.categorySpinner.setAdapter(adapter)
                }
            }
        }
    }

    /**
     * Créez un adaptateur personnalisé pour afficher uniquement le nom des catégories
     */
    fun createAdapter(categories: List<Category>): ArrayAdapter<Category> {
        val adapter = object : ArrayAdapter<Category>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val category = getItem(position)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = category?.nom ?: ""
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val category = getItem(position)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = category?.nom ?: ""
                return view
            }
        }
        return adapter
    }

    fun createAdapterAccount(accounts: List<Compte>): ArrayAdapter<Compte> {
        val adapter = object : ArrayAdapter<Compte>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            accounts
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val account = getItem(position)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = account?.nom ?: ""
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val account = getItem(position)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = account?.nom ?: ""
                return view
            }
        }
        return adapter
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
        val categoryId = binding.categorySpinner.text.toString()
        val date = getDate()
        val montantString = binding.transactionPrice.text.toString()
        val montant: Double = montantString.toDoubleOrNull() ?: 0.0
        var type = true
        if (montant >= 0) {
            type = true
        } else {
            type = false
        }

        val updatedTransaction = Transaction(transaction?.id ?: 0, name, 0, 0, date, montant, "", "", type)
        transactionViewModel.saveOrUpdateTransaction(updatedTransaction)
        dismiss()
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

    fun getCategory() {
        val spinner = binding.categorySpinner
        val categories = categoryListViewModel.categories
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Récupérer la catégorie sélectionnée à partir de la liste
                val selectedCategory = categories.value.get(position)

                // Récupérer l'ID de la catégorie sélectionnée
                val categoryId = selectedCategory.id

                // Utilisez l'ID de la catégorie selon vos besoins
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Aucune action requise si rien n'est sélectionné
                val selectedCategory = categories.value.get(0)
                val categoryId = selectedCategory.id
            }
        }
    }
}