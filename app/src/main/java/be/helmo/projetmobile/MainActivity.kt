package be.helmo.projetmobile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.databinding.ActivityMainBinding
import be.helmo.projetmobile.view.AccountFragment
import be.helmo.projetmobile.view.TransactionListFragment
import be.helmo.projetmobile.viewmodel.UserViewModel
import be.helmo.projetmobile.viewmodel.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository.get())
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**val headerLayout = LayoutInflater.from(this).inflate(R.layout.fragment_header, null)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = headerLayout
        supportActionBar?.setDisplayShowTitleEnabled(false)*/


        bottomNav = binding.bottomNav
        bottomNav.visibility = View.GONE
        observeUserExistence()
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment(), true)
                    true
                }

                R.id.categories -> {
                    loadFragment(CategoriesFragment(), true)
                    true
                }

                R.id.accounts -> {
                    loadFragment(AccountFragment(), true)
                    true
                }

                R.id.transaction ->  {
                    loadFragment(TransactionFragment(), true)
                    true
                }

                R.id.statistiques -> {
                    loadFragment(StatistiqueFragment(), true)
                    true
                }

                else -> {true}
            }
        }
    }

    fun loadFragment(fragment: Fragment, showBottomNav: Boolean) {
        if (showBottomNav) {
            bottomNav.visibility = View.VISIBLE
        } else {
            bottomNav.visibility = View.GONE
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutContainer, fragment)
        transaction.commit()
    }

    private fun observeUserExistence() {
        userViewModel.userExists.observe(this) { exists ->
            if (exists) {
                loadFragment(HomeFragment(), true)
            } else {
                loadFragment(LoginFragment(), false)
            }
        }
    }

}