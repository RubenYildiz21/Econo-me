package be.helmo.projetmobile

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutContainer, fragment)
        transaction.commit()
        bottomNav.visibility = if (showBottomNav) View.VISIBLE else View.GONE
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