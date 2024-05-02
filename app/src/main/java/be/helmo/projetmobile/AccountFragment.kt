package be.helmo.projetmobile.view

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.HeaderFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.TransactionDialogFragment
import be.helmo.projetmobile.TransferDialogFragment
import be.helmo.projetmobile.UserFragment
import be.helmo.projetmobile.databinding.FragmentAccountBinding

class AccountFragment : HeaderFragment(be.helmo.projetmobile.R.layout.fragment_categories) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val headerButton: ImageButton = view.findViewById(be.helmo.projetmobile.R.id.headerButton)
        headerButton.setOnClickListener{
            setupHeaderButton()
        }

        binding.addAccount.setOnClickListener {
            val addDialog = AccountDialogFragment.newInstance(null, Mode.CREATE)
            addDialog.show(childFragmentManager, "AddAccount")
        }


        binding.btnTransfer.setOnClickListener {
            showTransferDialog()
        }

        binding.AddExpense.setOnClickListener {
            val addDialog = TransactionDialogFragment.newInstance(null, Mode.EDIT, takePictureLauncher)
            addDialog.show(childFragmentManager, "AddTransaction")
        }
    }

    private fun setupHeaderButton() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(be.helmo.projetmobile.R.id.frameLayoutContainer, UserFragment())
            .addToBackStack(null)
            .commit()
    }
    private fun showTransferDialog() {
        Log.d("AccountFragment", "Entry showTransferDialog")
        val transferDialog = TransferDialogFragment.newInstance()
        transferDialog.show(childFragmentManager, "TransferDialog")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
