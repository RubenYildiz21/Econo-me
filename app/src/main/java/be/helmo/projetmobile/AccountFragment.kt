package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import be.helmo.projetmobile.databinding.FragmentAccountBinding

class AccountFragment : HeaderFragment(R.layout.fragment_categories) {
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

        val headerButton: ImageButton = view.findViewById(R.id.headerButton)
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
            .replace(R.id.frameLayoutContainer, UserFragment())
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
