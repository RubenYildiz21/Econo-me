package be.helmo.projetmobile

import android.app.Application
import be.helmo.projetmobile.database.CategoryDatabase
import be.helmo.projetmobile.database.CategoryRepository

class MobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CategoryDatabase.create(this)
        CategoryRepository.create(CategoryDatabase.get())
    }
}