package be.helmo.projetmobile.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CATEGORY")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM CATEGORY WHERE id=(:categoryId)")
    suspend fun getCategory(categoryId: Int): Category

    @Insert
    suspend fun addCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCat(category: Category)
}