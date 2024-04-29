package be.helmo.projetmobile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.viewmodel.UserViewModel
import be.helmo.projetmobile.viewmodel.UserViewModelFactory

abstract class HeaderFragment(layoutId: Int) : Fragment(layoutId) {
    private lateinit var headerUsernameTextView: TextView
    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(UserRepository.get()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerUsernameTextView = view.findViewById(R.id.header_username) ?: return
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            // Mettez à jour l'interface utilisateur ici
            headerUsernameTextView.text = user?.let { "${it.nom} ${it.prenom}" } ?: "Aucun utilisateur"
        }
    }
}
