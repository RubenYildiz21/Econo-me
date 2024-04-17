package be.helmo.projetmobile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.helmo.projetmobile.databinding.ListItemCategoryBinding
import be.helmo.projetmobile.model.Category
import java.util.UUID

class CategoryListAdapter(val categories: List<Category>, private val onCategoryClicked: (categoryId: UUID) -> Unit) : RecyclerView.Adapter<CategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(categories[position], onCategoryClicked)
    }
}

class CategoryHolder(val binding: ListItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category, onCategoryClicked: (categoryId: UUID) -> Unit) {
        binding.categoryName.text = category.nom
        binding.root.setOnClickListener {
            onCategoryClicked(category.id)
        }
    }
}
