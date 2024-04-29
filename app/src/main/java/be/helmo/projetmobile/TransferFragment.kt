package be.helmo.projetmobile

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.databinding.FragmentAccountTransfertBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import be.helmo.projetmobile.viewmodel.TransferViewModel
import kotlinx.coroutines.launch

class TransferFragment : Fragment() {
    private var _binding: FragmentAccountTransfertBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountListViewModel by viewModels()
    private val transferViewModel: TransferViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountTransfertBinding.inflate(inflater, container, false)
        Log.d("TransferFragment", "view creation")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("TransferFragment", "view created")
        super.onViewCreated(view, savedInstanceState)
        setupAccountsDropdown()
        setupTransferButton()
    }

    private fun setupAccountsDropdown() {
        viewLifecycleOwner.lifecycleScope.launch {
            accountViewModel.accounts.collect { accounts ->
                if (accounts.isNotEmpty()) {
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, accounts.map { it.nom })
                    binding.sourceAccount.setAdapter(adapter)
                    binding.destinationAccount.setAdapter(adapter)
                }else{
                    Log.d("TransferFragment", "setupAccountsDropdown not working")
                }
            }
        }
    }

    private fun setupTransferButton() {
        binding.transfertButton.setOnClickListener {
            Log.d("Bouton Transfert", "Clic sur le bouton de transfert détecté")
            val sourceAccountName = binding.sourceAccount.text.toString()
            val destinationAccountName = binding.destinationAccount.text.toString()
            val amount = binding.transferAmount.text.toString().toDouble()

            Log.d("Bouton Transfert", "Noms des comptes - Source: $sourceAccountName, Destination: $destinationAccountName, Montant: $amount")

            lifecycleScope.launch {
                transferViewModel.performTransfer(sourceAccountName, destinationAccountName, amount)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}