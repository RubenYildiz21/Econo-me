package be.helmo.projetmobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
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
import java.io.File
import java.util.Calendar
import java.util.Date
import be.helmo.projetmobile.model.Compte
import com.google.android.gms.maps.model.LatLng

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
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var photoFileName: String
    private var onPhotoComplete: (() -> Unit)? = null

    private var accounts: List<Compte> = listOf()

    companion object {
        fun newInstance(transaction: Transaction?, mode: Mode, takePictureLauncher: ActivityResultLauncher<Uri>?): TransactionDialogFragment {
            val fragment = TransactionDialogFragment()
            fragment.transaction = transaction
            fragment.mode = mode
            if (takePictureLauncher != null) {
                fragment.takePicture = takePictureLauncher
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionAddBinding.inflate(inflater, container, false)
        photoFileName = ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)

        loadCategories()
        loadAccounts()
        updateTransactionType()
        //googleMap()

        if (transaction?.facture != null) {
            photoFileName = transaction?.facture.toString()
        } else {
            val tokenLength = 10 // Longueur du token
            photoFileName = generateRandomToken(tokenLength)
        }
        val facturePhoto = binding.facturePhoto
        facturePhoto.setOnClickListener {
            val photoFile = File(requireContext().applicationContext.filesDir, photoFileName)
            val photoUri = FileProvider.getUriForFile(requireContext(), "be.helmo.projetmobile.fileprovider", photoFile)
            takePicture.launch(photoUri)
        }

        /**binding.addMap.setOnClickListener {
            showMapFragment()
        }*/

        binding.AddTransaction.setOnClickListener {
            submitData()
        }

        viewModel.onTransactionComplete = {
            dismiss()
        }
    }

    private fun showMapFragment() {
        val mapFragment = MapFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mapFragment)
            .addToBackStack(null) // Ajouter à la pile de retour si nécessaire
            .commit()
    }

    fun loadAccounts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountListViewModel.account.collect { account ->
                    accounts = account
                    if (account.isNotEmpty()) {
                        val name = account.map { "${it.nom} (${it.devise})"}
                        updateAccountDropdown(name)
                        /**transaction?.let { transaction ->
                            val accountId = transaction.compteId
                            val position = account.indexOfFirst { it.id == accountId }
                            if (position != -1) {
                                binding.accountSpinner.setSelection(position)
                            }
                        }*/
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
                    /**transaction?.let { transaction ->
                        val categoryId = transaction.categoryId
                        val position = categories.indexOfFirst { it.id == categoryId }
                        if (position != -1) {
                            binding.categorySpinner.setSelection(position)
                        }
                    }*/
                }
            }
        }
    }

    private fun updateCategoriesDropdown(name: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, name)
        binding.categorySpinner.setAdapter(adapter)
    }

    private fun updatePhoto(facturePhoto: ImageButton, photoFileName: String) {
        val photoFile = File(requireContext().applicationContext.filesDir, "t.jpg")
        if (photoFile.exists()) {
            // Construire le nouveau nom de fichier
            val newPhotoFile = File(photoFile.parentFile, photoFileName)

            // Renommer le fichier
            photoFile.renameTo(newPhotoFile)

            onPhotoComplete?.invoke()
        } else {
            facturePhoto.setImageResource(R.drawable.camera_solid)
        }
    }

    private fun updateTransactionType() {
        // Chargez les types de transactions à partir des ressources.
        val transactionTypes = resources.getStringArray(R.array.transaction_type)

        // Créer un ArrayAdapter utilisant un layout simple pour le dropdown.
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, transactionTypes)

        // Assurez-vous que votre layout a un AutoCompleteTextView ou Spinner avec l'ID approprié.
        binding.typeSpinner.setAdapter(adapter)

        // Si vous gérez les types de transactions pour les éditer, vous pouvez définir le type actuel ici.
        transaction?.let {
            val typeIndex = transactionTypes.indexOf(if (it.type) "Revenu" else "Dépense")
            binding.typeSpinner.setText(transactionTypes[typeIndex], false)
        }
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
            onPhotoComplete = null
            viewModel.onTransactionComplete = null
            nameEditText.setText(it.nom)
            binding.transactionPrice.setText(it.solde.toString())

            // Extraction de l'année, du mois et du jour de la date de la transaction
            val calendar = Calendar.getInstance()
            calendar.time = transaction?.date ?: Date()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Mise à jour du DatePicker avec la date de la transaction
            binding.datePicker.init(year, month, day, null)

        }
    }


    private fun submitData() {
        val name = nameEditText.text.toString()
        if (name == "") {
            Toast.makeText(context, "Veuillez entrer un nom pour la transaction.", Toast.LENGTH_LONG).show()
            return
        }

        val typetransaction = binding.typeSpinner.text.toString()
        if (typetransaction == "") {
            Toast.makeText(context, "Veuillez choisir un type pour la transaction.", Toast.LENGTH_LONG).show()
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
        if (typetransaction == "Dépense") {
            type = false
        } else {
            type = true
        }

        val selectedAccount = accounts.find { it.nom == compte }
        val currency = selectedAccount?.devise.toString()
        val updatedTransaction = Transaction(transaction?.id ?: 0, name, 0, 0, date, montant, LatLng(0.0, 0.0), currency, photoFileName, type)
        updatePhoto(binding.facturePhoto, photoFileName)
        viewModel.saveOrUpdateTransaction(compte, category, montant, updatedTransaction)

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

    fun generateRandomToken(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') // Caractères autorisés pour le token
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}