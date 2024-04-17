package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.helmo.projetmobile.databinding.FragmentCategoriesBinding
import be.helmo.projetmobile.databinding.FragmentHomeBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.view.CategoryListFragment
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.CategoryViewModel
import be.helmo.projetmobile.viewmodel.CategoryViewModelFactory
import kotlinx.coroutines.launch
import java.io.Console

class CategoriesFragment: Fragment() {
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
        binding.addCategoryButton.setOnClickListener {
            showCategoryDialog()
        }
    }

    private fun showCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.fragment_category_add, null)
        val editText = dialogView.findViewById<EditText>(R.id.category_name)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_category))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.ajouter)) { dialog, which ->
                // Effectuez le transfert ici
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        var name = editText.text.toString()
                        categoryViewModel.addCategory(name)
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}