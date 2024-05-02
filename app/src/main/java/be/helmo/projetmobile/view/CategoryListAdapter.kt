package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import be.helmo.projetmobile.databinding.ListItemCategoryBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import java.util.UUID

/**
 * Utilise list_item_category comme model pour afficher les categories en bd grace a ce model
 */
class CategoryListAdapter(
    val categories: List<Category>,
    private val onEditClicked: (categoryId: Int) -> Unit,
    private val onDeleteClicked: (categoryId: Int) -> Unit
) : RecyclerView.Adapter<CategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(categories[position], onEditClicked, onDeleteClicked)
    }

}

class CategoryHolder(val binding: ListItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category, onEditClicked: (categoryId: Int) -> Unit, onDeleteClicked: (accountId: Int) -> Unit) {
        binding.categoryName.text = category.nom
        binding.categoryPrice.text = String.format("%.2fEUR", category.solde)
        binding.editCategory.setOnClickListener {
            onEditClicked(category.id)
        }
        binding.deleteCategory.setOnClickListener{
            onDeleteClicked(category.id)
        }
    }
}
