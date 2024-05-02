package be.helmo.projetmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.databinding.FragmentLoginBinding
import be.helmo.projetmobile.viewmodel.UserViewModel
import be.helmo.projetmobile.viewmodel.UserViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository.get())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLogin()
    }

    private fun setupLogin() {
        binding.addUser.setOnClickListener {
            val name = binding.username.text.toString()
            val lastName = binding.userLastname.text.toString()
            if (name.isNotBlank() && lastName.isNotBlank()) {
                userViewModel.addUser(name, lastName)
                navigateToMain()
            } else {
                Toast.makeText(context, "Merci d'entrer votre nom et prénom", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        //(activity as? MainActivity)?.loadFragment(HomeFragment(), true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}