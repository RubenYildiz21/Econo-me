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
import be.helmo.projetmobile.databinding.FragmentAccountListBinding
import be.helmo.projetmobile.databinding.FragmentCategoryListBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
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
                    binding.accountRecyclerView.adapter = AccountListAdapter(accounts) {id ->
                        showAccount(id)
                    }
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
}