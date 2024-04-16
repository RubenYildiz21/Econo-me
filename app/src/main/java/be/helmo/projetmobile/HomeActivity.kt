package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import be.helmo.projetmobile.databinding.ActivityHomeBinding

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.localClassName, "onCreate called")

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}