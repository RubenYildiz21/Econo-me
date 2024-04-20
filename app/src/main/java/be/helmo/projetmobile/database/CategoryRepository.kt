package be.helmo.projetmobile.database

import be.helmo.projetmobile.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryRepository private constructor(val database: ProjectDatabase, private val coroutineScope: CoroutineScope = GlobalScope) {

    fun getCategories(): Flow<List<Category>> = database.categoryDao().getCategories()

    suspend fun getCategory(categoryId: Int): Category = database.categoryDao().getCategory(categoryId)

    suspend fun addCategory(category: Category) = database.categoryDao().addCategory(category)

    fun updateCategory(category: Category) {
        coroutineScope.launch {
            database.categoryDao().updateCategory(category)
        }
    }

    companion object {
        private var INSTANCE: CategoryRepository? = null

        fun create(database: ProjectDatabase) {
            INSTANCE = CategoryRepository(database)
        }

        fun get(): CategoryRepository {
            if(INSTANCE == null)
                INSTANCE = CategoryRepository(ProjectDatabase.get())
            return INSTANCE!!
        }
    }
}