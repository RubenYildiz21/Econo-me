package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.databinding.FragmentTransactionBinding
import be.helmo.projetmobile.view.TransactionListFragment
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import be.helmo.projetmobile.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.launch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import be.helmo.placereport.view.getScaledBitmap
import java.io.File

class TransactionFragment: HeaderFragment(R.layout.fragment_transaction) {
    private lateinit var binding: FragmentTransactionBinding
    private val viewModel: TransactionListViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository.get(),
            ViewModelProvider(requireActivity()).get(AccountListViewModel::class.java),
            ViewModelProvider(requireActivity()).get(CategoryListViewModel::class.java)
        )
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMonthDropdown()

        binding.month.setOnItemClickListener { parent, view, position, id ->
            val months = resources.getStringArray(R.array.months_array)
            val selectedMonth = months[position]
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val transactionListFragment = childFragmentManager.findFragmentById(R.id.container) as TransactionListFragment?
                    transactionListFragment?.updateTransactionsByMonth(position)
                }
            }
        }

        binding.AddTransaction.setOnClickListener {
            val addDialog = TransactionDialogFragment.newInstance(null, Mode.CREATE, takePictureLauncher)
            addDialog.show(childFragmentManager, "AddTransaction")
        }
    }

    private fun setupMonthDropdown() {
        // Récupérer le tableau de mois depuis les ressources
        val months = resources.getStringArray(R.array.months_array)

        // Créer un adaptateur pour le AutoCompleteTextView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, months)

        // Associer l'adaptateur au AutoCompleteTextView
        binding.month.setAdapter(adapter)

    }

    private fun updatePhoto(facturePhoto: ImageButton, photoFileName: String) {
        val photoFile = File(requireContext().applicationContext.filesDir, photoFileName)
        if (photoFile.exists()) {
            facturePhoto.doOnLayout {
                val photo = getScaledBitmap(photoFile.path, it.width, it.height)
                facturePhoto.setImageBitmap(photo)
            }
        } else {
            facturePhoto.setImageResource(R.drawable.camera_solid)
        }
    }
}
