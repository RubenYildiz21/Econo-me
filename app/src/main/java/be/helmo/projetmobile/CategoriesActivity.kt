package be.helmo.projetmobile


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.MenuHost
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import be.helmo.projetmobile.databinding.ActivityCategoriesBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.view.CategoryListAdapter
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.UUID

class CategoriesActivity : FragmentActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //supportFragmentManager.beginTransaction().add(R.id.fragment_container, CategoryListFragment()).commit()

        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.new_cat -> {
                    true
                }

                else -> {true}
            }
        }

        /**binding.addCategory.setOnClickListener {
            lifecycleScope.launch {
                categoryListViewModel.categories.collect { categories ->
                    Log.d("CategoriesActivity", "Total categories: ${categories.size}")
                    binding.categoryRecyclerView.adapter = CategoryListAdapter(categories) { id ->
                        showCategory(id)
                    }
                }
            }
        }*/
    }

    private fun showNewCategory() {
        /**viewLifecycleOwner.lifecycleScope.launch {
            val id = categoryListViewModel.addCategory()
            showCategory(id)
        }*/
    }

    private fun showCategory(id : UUID) {
        /**findNavController().navigate(
        CategoryListFragmentDirections.showCategory(
        id
        )
        )*/
    }
}
