package be.helmo.projetmobile

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.databinding.ActivityMainBinding
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

        if (!isNetworkAvailable(this)) {
            // Afficher un message d'erreur ou fermer l'application
            Toast.makeText(this, "Une connection internet est necessaire pour utiliser l'application", Toast.LENGTH_LONG).show()
            finish()
        }
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

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}