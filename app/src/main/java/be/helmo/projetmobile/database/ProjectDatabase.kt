package be.helmo.projetmobile.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.model.Transfere
import be.helmo.projetmobile.model.User

@Database(entities = [Category::class, User::class, Compte::class, Transaction::class, Transfere::class], version = 1, exportSchema = false)
@TypeConverters(ItemsConverter::class, LatLngConverter::class)
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun compteDao(): CompteDao
    abstract fun userDao(): UserDao
    abstract fun transferDao(): TransfereDao
    companion object {
        private var INSTANCE: ProjectDatabase? = null

        fun create(context: Context) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ProjectDatabase::class.java,
                "mobile-database")
                .build()
        }

        fun get(): ProjectDatabase {
            return INSTANCE ?: throw IllegalStateException("Database must be initialized")
        }
    }
}