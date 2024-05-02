package be.helmo.projetmobile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.graphics.Point
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.database.UserRepository
import be.helmo.projetmobile.databinding.ActivityMainBinding
import be.helmo.projetmobile.view.AccountFragment
import be.helmo.projetmobile.view.TransactionListFragment
import be.helmo.projetmobile.viewmodel.UserViewModel
import be.helmo.projetmobile.viewmodel.UserViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    //OnMapClickListener,
    //OnMapReadyCallback
    private lateinit var bottomNav: BottomNavigationView
    //private lateinit var mGoogleMap: GoogleMap

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository.get())
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_map)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)


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

    /**override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setPadding(0, 0, 0, 0)
        mGoogleMap = googleMap
        /**googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )*/

        googleMap.setOnMapClickListener(this)
        val userPos = enableMyLocation()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPos, 15.0f))
    }

    private fun enableMyLocation(): LatLng {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap?.setMyLocationEnabled(true)

            // Obtenir la dernière position connue de l'utilisateur
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var userLocation: LatLng = LatLng(0.0, 0.0)

            // Vérifier si la dernière position connue est disponible
            lastKnownLocation?.let {
                // Placer un marqueur sur la position de l'utilisateur
                userLocation = LatLng(it.latitude, it.longitude)
                mGoogleMap?.addMarker(
                    MarkerOptions()
                        .position(userLocation)
                        .title("Ma position")
                )
            }

            return userLocation
        }
        return LatLng(0.0, 0.0)
    }

    override fun onMapClick(point: LatLng) {
        mGoogleMap?.clear()
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(point)
                .title("Marker")
        )
    }*/

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