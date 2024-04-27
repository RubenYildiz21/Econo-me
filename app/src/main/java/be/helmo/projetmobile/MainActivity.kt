package be.helmo.projetmobile

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.databinding.ActivityMainBinding
import be.helmo.projetmobile.view.AccountFragment
import coil.ImageLoader
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**val headerLayout = LayoutInflater.from(this).inflate(R.layout.fragment_header, null)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = headerLayout
        supportActionBar?.setDisplayShowTitleEnabled(false)*/

        loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)!!
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.categories -> {
                    loadFragment(CategoriesFragment())
                    true
                }

                R.id.accounts -> {
                    loadFragment(AccountFragment())
                    true
                }

                else -> {true}
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutContainer, fragment)
        transaction.commit()
    }

}