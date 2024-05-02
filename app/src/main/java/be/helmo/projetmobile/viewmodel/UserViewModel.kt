package be.helmo.projetmobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.model.User
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    val userExists = userRepository.hasUsers.asLiveData()

    val userLiveData = userRepository.getUserLiveData()
    fun addUser(nom: String, prenom: String) {
        val newUser = User(0, nom = nom, prenom = prenom)
        viewModelScope.launch {
            userRepository.addUser(newUser)
        }
    }

    fun updateUser(nom: String, prenom: String) {
        viewModelScope.launch {
            val currentUser = userLiveData.value ?: User(id = 0, nom = "", prenom = "")
            val updatedUser = currentUser.copy(nom = nom, prenom = prenom)
            userRepository.updateUser(updatedUser)
        }
    }

}