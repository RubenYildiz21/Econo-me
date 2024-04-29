package be.helmo.projetmobile.view

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.HeaderFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.databinding.FragmentAccountBinding

class AccountFragment : HeaderFragment(be.helmo.projetmobile.R.layout.fragment_categories) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addAccount.setOnClickListener {
            val addDialog = AccountDialogFragment.newInstance(null, Mode.CREATE)
            addDialog.show(childFragmentManager, "AddAccount")
        }


        binding.btnTransfer.setOnClickListener {
            // implémenter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
