package be.helmo.projetmobile.view

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
import be.helmo.projetmobile.databinding.FragmentAccountListBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import kotlinx.coroutines.launch

class AccountListFragment : Fragment() {

    private val accountListViewModel: AccountListViewModel by viewModels()

    private lateinit var binding: FragmentAccountListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountListBinding.inflate(inflater, container, false)
        binding.accountRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        //menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountListViewModel.account.collect() { accounts ->
                    Log.d("AccountListFragment", "Total accounts: ${accounts.size}")
                    binding.accountRecyclerView.adapter = AccountListAdapter(accounts, ::showAccount, ::showEditAccountDialog)
                }
            }
        }
    }

    private fun showNewAccount() {
        viewLifecycleOwner.lifecycleScope.launch {
            //val id = categoryListViewModel.addCategory("")
            showAccount(id)
        }
    }

    private fun showAccount(id : Int) {
        /**findNavController().navigate(
        CategoryListFragmentDirections.showCategory(
        id
        )
        )*/
    }

    private fun showEditAccountDialog(id: Int) {
        val account = accountListViewModel.account.value.find { it.id == id }
        if (account != null) {
            val editDialog = AccountDialogFragment.newInstance(account, Mode.EDIT) // passer le compte à modifier
            editDialog.show(childFragmentManager, "EditAccount")
        }
    }
}