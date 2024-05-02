package be.helmo.projetmobile.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.FragmentDetailTransaction
import be.helmo.projetmobile.MapFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.R
import be.helmo.projetmobile.TransactionDialogFragment
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.databinding.FragmentTransactionListBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import be.helmo.projetmobile.viewmodel.TransactionViewModelFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TransactionListFragment: Fragment(), MapFragment.OnLatLngSelectedListener {
    private val viewModel: TransactionListViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository.get(),
            ViewModelProvider(requireActivity()).get(AccountListViewModel::class.java),
            ViewModelProvider(requireActivity()).get(CategoryListViewModel::class.java)
        )
    }
    private val accountViewModel: AccountListViewModel by viewModels()
    private val categoryViewModel: CategoryListViewModel by viewModels()
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {

        }
    }

    private var transacToUpdate: Int = 0
    private lateinit var binding: FragmentTransactionListBinding
    private val accountListViewModel: AccountListViewModel by viewModels()
    private val categoryListViewModel: CategoryListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransactionList()
    }

    private fun setupTransactionList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.transaction,
                    accountListViewModel.account,
                    categoryListViewModel.categories
                ) { transactions, accounts, categories ->
                    Triple(transactions, accounts, categories)
                }.collect { (transactions, accounts, categories) ->
                    if (transactions.isNotEmpty() && accounts.isNotEmpty() && categories.isNotEmpty()) {
                        binding.transactionRecyclerView.adapter = TransactionListAdapter(
                            transactions,
                            accounts,
                            categories,
                            ::showEditAccountDialog,
                            ::deleteAccount,
                            ::showMapFragment,
                            ::onTransactionClicked
                        )
                    } else {
                        Log.d("Debug", "Data missing: Transactions or Accounts or Categories")
                    }
                }
            }
        }
    }

    fun updateTransactionsByMonth(selectedMonth: Int) {
        viewModel.loadTransactionsByMonth(selectedMonth)
        setupTransactionList()
    }


    private fun showEditAccountDialog(id: Int) {
        val transac = viewModel.transaction.value.find { it.id == id }
        if (transac != null) {
            val editDialog = TransactionDialogFragment.newInstance(transac, Mode.EDIT, takePictureLauncher) // passer le compte à modifier
            editDialog.show(childFragmentManager, "EditAccount")
        }
    }

    private fun deleteAccount(id: Int) {
        val transac = viewModel.transaction.value.find { it.id == id }
        transac?.let { trans ->
            AlertDialog.Builder(requireContext())
                .setTitle("ATTENTION")
                .setMessage("Etes-vous sur de vouloir supprimer cette transaction : ${trans.nom}?")
                .setPositiveButton("Supprimer") { dialog, which ->
                    viewModel.deleteTransaction(trans)
                    categoryViewModel.updateCategoryAfterDelete(trans.solde, trans.categoryId)
                    accountViewModel.updateAccountAfterDelete(trans.solde, trans.compteId, trans.type)
                    setupTransactionList()
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showMapFragment(id: Int) {
        val mapFragment = MapFragment()
        transacToUpdate = id
        /**val editPlace = MapFragment()
        editPlace.show(childFragmentManager, "EditPlace")*/

        mapFragment.setOnLatLngSelectedListener(this)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, mapFragment)
            .addToBackStack(null) // Ajouter à la pile de retour si nécessaire
            .commit()

    }

    fun onTransactionClicked(id: Int) {
        val tr = viewModel.getTransactioByID(id)
        val transactionDetail = FragmentDetailTransaction.newInstance(tr)
        transacToUpdate = id

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, transactionDetail)
            .addToBackStack(null) // Ajouter à la pile de retour si nécessaire
            .commit()
    }

    override fun onLatLngSelected(latLng: LatLng) {
        viewModel.updateLieu(transacToUpdate, latLng)
    }
}