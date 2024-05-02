package be.helmo.projetmobile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import be.helmo.projetmobile.databinding.FragmentHomeBinding
import be.helmo.projetmobile.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment() : Fragment(),
    GoogleMap.OnMapClickListener,
    OnMapReadyCallback {
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var pos: LatLng
    private lateinit var binding: FragmentMapBinding
    private var latLngListener: OnLatLngSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.myButton.setOnClickListener {
            savePosition()
            requireActivity().supportFragmentManager.popBackStack()
        }
        /**val button = childFragmentManager.findFragmentById(R.id.myButton) as Button
        button.setOnClickListener {
            savePosition()
        }*/
    }

    private fun savePosition() {
        latLngListener?.onLatLngSelected(pos)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setPadding(0, 0, 0, 200)
        mGoogleMap = googleMap
        /**googleMap.addMarker(
        MarkerOptions()
        .position(LatLng(0.0, 0.0))
        .title("Marker")
        )*/

        googleMap.setOnMapClickListener(this)
        pos = enableMyLocation()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.0f))
    }

    private fun enableMyLocation(): LatLng {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.setMyLocationEnabled(true)

            // Obtenir la dernière position connue de l'utilisateur
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var userLocation: LatLng = LatLng(0.0, 0.0)

            // Vérifier si la dernière position connue est disponible
            lastKnownLocation?.let {
                // Placer un marqueur sur la position de l'utilisateur
                userLocation = LatLng(it.latitude, it.longitude)
                mGoogleMap.addMarker(
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
        mGoogleMap.clear()
        pos = point
        mGoogleMap.addMarker(
            MarkerOptions()
                .position(point)
                .title("Marker")
        )
    }

    fun setOnLatLngSelectedListener(listener: OnLatLngSelectedListener) {
        latLngListener = listener
    }

    interface OnLatLngSelectedListener {
        fun onLatLngSelected(latLng: LatLng)
    }
}