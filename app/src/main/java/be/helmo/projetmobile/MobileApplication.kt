package be.helmo.projetmobile

import android.app.Application
import be.helmo.projetmobile.database.ProjectDatabase
import be.helmo.projetmobile.database.CategoryRepository

class MobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ProjectDatabase.create(this)
        //ProjectDatabase.create(ProjectDatabase.get())
    }
}