package be.helmo.projetmobile.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import be.helmo.projetmobile.model.Category

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    companion object {
        private var INSTANCE: CategoryDatabase? = null

        fun create(context: Context) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                CategoryDatabase::class.java,
                "mobile-database")
                .build()
        }

        fun get(): CategoryDatabase {
            return INSTANCE ?: throw IllegalStateException("CategoryDatabase must be initialized")
        }
    }
}