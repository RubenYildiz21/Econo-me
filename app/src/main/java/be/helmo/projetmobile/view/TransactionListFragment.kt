package be.helmo.projetmobile.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.TransactionDialogFragment
import be.helmo.projetmobile.databinding.FragmentAccountListBinding
import be.helmo.projetmobile.databinding.FragmentTransactionListBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import kotlinx.coroutines.launch

class TransactionListFragment: Fragment() {
    private val transactionListViewModel: TransactionListViewModel by viewModels()

    private lateinit var binding: FragmentTransactionListBinding

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

        val menuHost: MenuHost = requireActivity()
        //menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transactionListViewModel.transaction.collect() { transactions ->
                    Log.d("AccountListFragment", "Total accounts: ${transactions.size}")
                    binding.transactionRecyclerView.adapter = TransactionListAdapter(transactions, ::showEditAccountDialog, ::deleteAccount)
                }
            }
        }
    }


    private fun showEditAccountDialog(id: Int) {
        val transac = transactionListViewModel.transaction.value.find { it.id == id }
        if (transac != null) {
            val editDialog = TransactionDialogFragment.newInstance(transac, Mode.EDIT) // passer le compte à modifier
            editDialog.show(childFragmentManager, "EditAccount")
        }
    }

    private fun deleteAccount(id: Int) {
        val transac = transactionListViewModel.transaction.value.find { it.id == id }
        transac?.let { trans ->
            AlertDialog.Builder(requireContext())
                .setTitle("ATTENTION")
                .setMessage("Etes-vous sur de vouloir supprimer ce compte : ${trans.nom}?")
                .setPositiveButton("Supprimer") { dialog, which ->
                    transactionListViewModel.deleteTransaction(trans)
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}