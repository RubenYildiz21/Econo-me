package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {

    @Query("SELECT * FROM USER")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM USER WHERE id=(:userId)")
    suspend fun getUser(userId: UUID): User

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}