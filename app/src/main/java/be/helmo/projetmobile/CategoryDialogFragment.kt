package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.databinding.FragmentAccountAddBinding
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.Mode
import be.helmo.projetmobile.databinding.FragmentCategoryAddBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryDialogFragment : BottomSheetDialogFragment() {
    private lateinit var confirmButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var dialogName: TextView
    private var category: Category? = null  // Compte pour la modification
    private val categoryViewModel: CategoryListViewModel by viewModels()
    private var _binding: FragmentCategoryAddBinding? = null
    private val binding get() = _binding!!

    private var mode: Mode = Mode.CREATE

    companion object {
        fun newInstance(category: Category?, mode: Mode): CategoryDialogFragment {
            val fragment = CategoryDialogFragment()
            fragment.category = category
            fragment.mode = mode
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        confirmButton.setOnClickListener {
            submitData()
        }
    }

    private fun setupViews(view: View) {
        confirmButton = view.findViewById(R.id.addCat)
        nameEditText = view.findViewById(R.id.category_name_adding)
        dialogName = view.findViewById(R.id.dialogName)

        // Mettre à jour le texte du bouton en fonction du mode
        if (mode == Mode.EDIT) {
            confirmButton.text = "Modifier"
            dialogName.text = "Modifier la categorie"
        }
        // Charger les données existantes si en mode 'Modifier'
        category?.let {
            nameEditText.setText(it.nom)

        }
    }


    private fun submitData() {
        val name = nameEditText.text.toString()
        val updatedCategory = Category(category?.id ?: 0, name, 0.0)
        categoryViewModel.saveOrUpdateCategory(updatedCategory)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
