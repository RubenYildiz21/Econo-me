package be.helmo.projetmobile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            when (it.key) {
                Manifest.permission.CAMERA -> {
                    if (!it.value) {
                        Toast.makeText(this, "L'accès à la caméra est nécessaire pour certaines fonctionnalités.", Toast.LENGTH_LONG).show()
                    }
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    if (!it.value) {
                        Toast.makeText(this, "L'accès à la localisation est nécessaire pour certaines fonctionnalités.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        // Continuer à charger l'interface utilisateur même si les permissions sont refusées
        setupUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Une connection internet est nécessaire pour utiliser l'application", Toast.LENGTH_LONG).show()
            finish()
        } else {
            checkPermissionsAndSetupUI()
        }
    }

    private fun checkPermissionsAndSetupUI() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
            else -> setupUI()
        }
    }

    private fun setupUI() {
        bottomNav = findViewById(R.id.bottomNav)
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
                R.id.transaction -> {
                    loadFragment(TransactionFragment(), true)
                    true
                }
                R.id.statistiques -> {
                    loadFragment(StatistiqueFragment(), true)
                    true
                }
                else -> true
            }
        }
    }

    fun loadFragment(fragment: Fragment, showBottomNav: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, fragment)
            .commit()
        bottomNav.visibility = if (showBottomNav) View.VISIBLE else View.GONE
    }

    private fun observeUserExistence() {
        userViewModel.userExists.observe(this) { exists ->
            if (exists) loadFragment(HomeFragment(), true)
            else loadFragment(LoginFragment(), false)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }
}
