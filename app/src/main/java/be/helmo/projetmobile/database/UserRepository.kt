package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class UserRepository private constructor(val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getUsers(): Flow<List<User>> = database.userDao().getUsers()

    suspend fun getUser(userId: UUID): User = database.userDao().getUser(userId)

    suspend fun addUser(user: User) = database.userDao().addUser(user)

    fun updateUser(user: User) {
        coroutineScope.launch {
            database.userDao().updateUser(user)
        }
    }

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