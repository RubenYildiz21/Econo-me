package be.helmo.projetmobile.view

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.databinding.FragmentAccountAddBinding
import be.helmo.projetmobile.databinding.FragmentAccountBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private var _addAccount_binding: FragmentAccountAddBinding? = null
    private val binding get() = _binding!!
    private val addBinding get() = _addAccount_binding!!

    private val accountViewModel: AccountListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        _addAccount_binding = FragmentAccountAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrencyDropdown()
        binding.addAccount.setOnClickListener {
            val addDialog = AccountDialogFragment.newInstance(null)
            addDialog.show(childFragmentManager, "AddAccount")
        }



        binding.btnTransfer.setOnClickListener {
            // implémenter
        }
    }

    private fun setupCurrencyDropdown() {
        val currencies = resources.getStringArray(be.helmo.projetmobile.R.array.currency_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, currencies)
        addBinding.accountCurrency.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid leaking views
    }

}
