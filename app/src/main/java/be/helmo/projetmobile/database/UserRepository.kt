package be.helmo.projetmobile.database

import androidx.lifecycle.LiveData
import be.helmo.projetmobile.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class UserRepository private constructor(val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getUsers(): Flow<List<User>> = database.userDao().getUsers()

    fun getUserLiveData(): LiveData<User?> {
        // Convertir Flow<List<User>> en LiveData<User?>
        return getUsers().map { users -> users.firstOrNull() }.asLiveData()
    }

    suspend fun addUser(user: User) = database.userDao().addUser(user)

    fun updateUser(user: User) {
        coroutineScope.launch {
            database.userDao().updateUser(user)
        }
    }

    // Vérifier si un utilisateur existe
    val hasUsers: Flow<Boolean> = getUsers().map { it.isNotEmpty() }

    companion object {
        private var INSTANCE: UserRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = UserRepository(database)
        }

        fun get(): UserRepository {
            if(INSTANCE == null)
                INSTANCE = UserRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}