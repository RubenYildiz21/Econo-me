package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class ModalBottomSheet: BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var editText: EditText
    private val categoryViewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_category_add, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton = view.findViewById(R.id.addCat)
        val dialogView = layoutInflater.inflate(R.layout.fragment_category_add, null)
        //val editText = dialogView.findViewById<EditText>(R.id.category_name_adding)
        editText = view.findViewById(R.id.category_name_adding)

        confirmButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    var name = editText.text.toString()
                    categoryViewModel.addCategory(name)
                    dismiss()
                }
            }
        }
    }
}