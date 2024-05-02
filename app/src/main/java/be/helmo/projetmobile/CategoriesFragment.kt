package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import be.helmo.projetmobile.databinding.FragmentCategoriesBinding
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import kotlinx.coroutines.launch

class CategoriesFragment: HeaderFragment(R.layout.fragment_categories) {
    private lateinit var binding: FragmentCategoriesBinding
    private val categoryViewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        //supportFragmentManager.beginTransaction().add(R.id.fragment_container, CategoryListFragment()).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirm.setOnClickListener {
            val modalBottomSheet = ModalBottomSheet()
            modalBottomSheet.show(childFragmentManager, ModalBottomSheet.TAG)
            //showCategoryDialog()
        }
        val headerButton: ImageButton = view.findViewById(be.helmo.projetmobile.R.id.headerButton)
        headerButton.setOnClickListener{
            setupHeaderButton()
        }
    }

    private fun setupHeaderButton() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, UserFragment())
            .addToBackStack(null)
            .commit()
    }
}