package be.helmo.projetmobile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import be.helmo.placereport.view.getScaledBitmap
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.databinding.FragmentTransactionDetailBinding
import be.helmo.projetmobile.model.Transaction
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import be.helmo.projetmobile.viewmodel.CategoryListViewModel
import be.helmo.projetmobile.viewmodel.TransactionListViewModel
import be.helmo.projetmobile.viewmodel.TransactionViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File

class FragmentDetailTransaction : DialogFragment(),
    GoogleMap.OnMapClickListener,
    OnMapReadyCallback {
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var binding: FragmentTransactionDetailBinding
    private var id: Int = 0
    private lateinit var photoFileName: String
    private lateinit var pos: LatLng
    private val viewModel: TransactionListViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository.get(),
            ViewModelProvider(requireActivity()).get(AccountListViewModel::class.java),
            ViewModelProvider(requireActivity()).get(CategoryListViewModel::class.java)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(transaction: Transaction?): Fragment {
            val fragment = FragmentDetailTransaction()
            if (transaction != null) {
                fragment.id = transaction.id
                fragment.photoFileName = transaction.facture
                fragment.pos = transaction.lieu
            }
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val image = binding.factureTransac
        if (photoFileName != null) {
            updatePhoto(image, photoFileName)
        }

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setPadding(0, 0, 0, 200)
        mGoogleMap = googleMap
        googleMap.addMarker(
            MarkerOptions()
            .position(LatLng(pos.latitude, pos.longitude))
            .title("Marker")
        )

        googleMap.setOnMapClickListener(this)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.0f))
    }

    override fun onMapClick(p0: LatLng) {
        return
    }

    private fun updatePhoto(facturePhoto: ImageView, photoFileName: String) {
        if (photoFileName != "") {
            val photoFile = File(requireContext().applicationContext.filesDir, photoFileName)
            if (photoFile.exists()) {
                facturePhoto.doOnLayout {
                    val photo = getScaledBitmap(photoFile.path, it.width, it.height)
                    facturePhoto.setImageBitmap(photo)
                }
            } else {
                facturePhoto.setImageResource(R.drawable.camera_solid)
            }
        } else {
            facturePhoto.setImageResource(R.drawable.camera_solid)
        }
    }
}