package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.databinding.FragmentProfileBinding
import be.helmo.projetmobile.viewmodel.UserViewModel
import be.helmo.projetmobile.viewmodel.UserViewModelFactory
import okhttp3.internal.format

class UserFragment : HeaderFragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var headerUsernameTextView: TextView

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository.get())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerUsernameTextView = view.findViewById(R.id.nameOnProfile) ?: return
        viewModel.userLiveData.observe(viewLifecycleOwner) { user ->

            headerUsernameTextView.text = user?.let { "${it.nom} ${it.prenom}" } ?: "Aucun utilisateur"
        }

        binding.updateUser.setOnClickListener {
            val nom = binding.userName.text.toString()
            val prenom = binding.userLastname.text.toString()
            if (validateInputs(nom, prenom)) { // Validez vos entrées ici
                viewModel.updateUser(nom, prenom)
                Toast.makeText(context, "Profil mis a jour avec succes", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Champs incorrects", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(nom: String, prenom: String): Boolean {
        return nom.isNotBlank() && prenom.isNotBlank()
    }

}