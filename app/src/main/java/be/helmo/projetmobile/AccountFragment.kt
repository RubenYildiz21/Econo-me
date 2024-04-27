package be.helmo.projetmobile.view

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.AccountDialogFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.databinding.FragmentAccountAddBinding
import be.helmo.projetmobile.databinding.FragmentAccountBinding
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import kotlin.math.log

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val accountViewModel: AccountListViewModel by viewModels()

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
