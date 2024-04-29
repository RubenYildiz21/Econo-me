package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.helmo.projetmobile.databinding.FragmentAccountTransfertBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TransferDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAccountTransfertBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): TransferDialogFragment {
            return TransferDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountTransfertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
