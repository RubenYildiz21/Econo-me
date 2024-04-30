package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.helmo.projetmobile.databinding.FragmentAccountBinding
import be.helmo.projetmobile.databinding.FragmentCategoriesBinding
import be.helmo.projetmobile.databinding.FragmentTransactionBinding

class TransactionFragment: HeaderFragment(R.layout.fragment_transaction) {
    private lateinit var binding: FragmentTransactionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.AddTransaction.setOnClickListener {
            val addDialog = TransactionDialogFragment.newInstance(null, Mode.CREATE)
            addDialog.show(childFragmentManager, "AddTransaction")
        }
    }
}