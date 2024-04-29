package be.helmo.projetmobile

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import be.helmo.projetmobile.database.TransfereRepository
import be.helmo.projetmobile.databinding.FragmentAccountTransfertBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import be.helmo.projetmobile.viewmodel.TransferViewModel
import be.helmo.projetmobile.viewmodel.TransferViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class TransferDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAccountTransfertBinding? = null
    private val binding get() = _binding!!


    private val accountViewModel: AccountListViewModel by viewModels()
    private val transferViewModel: TransferViewModel by viewModels()
    private val viewModel: TransferViewModel by viewModels {
        TransferViewModelFactory(
            TransfereRepository.get(),
            ViewModelProvider(requireActivity()).get(CurrencyViewModel::class.java),
            ViewModelProvider(requireActivity()).get(AccountListViewModel::class.java)
        )
    }

    companion object {
        fun newInstance(): TransferDialogFragment {
            Log.d("TransferDialogFragment", "Création de l'instance")
            return TransferDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("TransferDialogFragment", "onCreateView")
        _binding = FragmentAccountTransfertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAccounts()
        binding.transfertBtn.setOnClickListener {
            initiateTransfer()
        }
        viewModel.onTransferCompleted = {
            dismiss()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initiateTransfer() {
        val sourceAccountInfo = binding.sourceAccount.text.toString().split(" (")[0]
        val destinationAccountInfo = binding.destinationAccount.text.toString().split(" (")[0]
        val amount = binding.transferAmount.text.toString().toDoubleOrNull()

        if (amount == null || amount <= 0) {
            Toast.makeText(context, "Veuillez entrer un montant valide.", Toast.LENGTH_LONG).show()
            return
        }

        viewModel.performTransfer(sourceAccountInfo, destinationAccountInfo, amount)
    }

    private fun loadAccounts() {
        lifecycleScope.launch {
            accountViewModel.accounts.collect { accounts ->
                if (accounts.isNotEmpty()) {
                    val namesWithCurrencies = accounts.map { "${it.nom} (${it.devise})" }
                    updateAccountDropdowns(namesWithCurrencies)
                }
            }
        }
    }

    private fun updateAccountDropdowns(accounts: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, accounts)
        binding.sourceAccount.setAdapter(adapter)
        binding.destinationAccount.setAdapter(adapter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onTransferCompleted = null
        _binding = null
    }
}
