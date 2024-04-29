package be.helmo.projetmobile.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.CategoryDialogFragment
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.R
import be.helmo.projetmobile.databinding.FragmentCategoryListBinding
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Affiche les categories presentent en bd
 */
class CategoryListFragment : Fragment() {

    private val categoryListViewModel: CategoryListViewModel by viewModels()

    private lateinit var binding: FragmentCategoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        //menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryListViewModel.categories.collect() { categories ->
                    Log.d("CategoryListFragment", "Total categories: ${categories.size}")
                    binding.categoryRecyclerView.adapter = CategoryListAdapter(categories, ::showEditAccountDialog, ::deleteCat)
                }
            }
        }
    }

    private fun showEditAccountDialog(id: Int) {
        val account = categoryListViewModel.categories.value.find { it.id == id }
        if (account != null) {
            val editDialog = CategoryDialogFragment.newInstance(account, Mode.EDIT)
            editDialog.show(childFragmentManager, "EditCategory")
        }
    }

    private fun deleteCat(id: Int) {
        val category = categoryListViewModel.categories.value.find { it.id == id }
        category?.let { cat ->
            AlertDialog.Builder(requireContext())
                .setTitle("ATTENTION")
                .setMessage("Etes-vous sur de vouloir supprimer ce compte : ${cat.nom}?")
                .setPositiveButton("Supprimer") { dialog, which ->
                    categoryListViewModel.deleteCat(cat)
                    dialog.dismiss()
                }
                .setNegativeButton("Annuler") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    /**
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_navbar, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.new_cat -> {
                showNewCategory()
                true
            }
            else -> false
        }
    }
    */

    private fun showNewCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            //val id = categoryListViewModel.addCategory("")
            showCategory(id)
        }
    }

    private fun showCategory(id : Int) {
        /**findNavController().navigate(
            CategoryListFragmentDirections.showCategory(
                id
            )
        )*/
    }
}