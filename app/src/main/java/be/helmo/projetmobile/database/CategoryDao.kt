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


    @Query("SELECT * FROM Category WHERE id IN (SELECT categoryId FROM `Transaction` WHERE strftime('%m', datetime(date / 1000, 'unixepoch')) = :month)")
    fun getCategoriesByMonth(month: String): Flow<List<Category>>

    @Query("Select * from Category where Category.id in (select tr.categoryId from 'Transaction' tr where tr.type = :transactionType)")
    fun getTypeTransactions(transactionType: Boolean): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id IN (SELECT categoryId FROM `Transaction` WHERE strftime('%Y', datetime(date / 1000, 'unixepoch')) = :year)")
    fun getCategoriesByYear(year: String): Flow<List<Category>>


    @Insert
    suspend fun addCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCat(category: Category)
}