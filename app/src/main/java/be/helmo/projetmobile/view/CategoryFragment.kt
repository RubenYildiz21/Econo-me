package be.helmo.projetmobile.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import be.helmo.projetmobile.databinding.FragmentCategoriesBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.CategoryViewModel
import be.helmo.projetmobile.viewmodel.CategoryViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryFragment: Fragment() {
    private lateinit var binding: FragmentCategoriesBinding

    //private val args: CategoryFragmentArgs by navArgs()

    /**private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(args.categoryId)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                /**categoryViewModel.category.collect {category ->
                    category?.let {
                        updateUi(it)
                    }
                }*/
            }
        }
    }

    private fun updateUi(category: Category) {
        /**binding.apply {
            //categoryName.doOnTextChanged { text, _, _, _ -> categoryViewModel.updateCategory { it.copy(nom = text.toString()) }}
            if (categoryName.text.toString() != category.nom)
                categoryName.setText(category.nom)
        }*/
    }
}