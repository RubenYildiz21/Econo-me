package be.helmo.projetmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import be.helmo.projetmobile.databinding.AccountsActivityBinding
import be.helmo.projetmobile.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.localClassName, "onCreate called")

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeButton.setOnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val accountBinding = AccountsActivityBinding.inflate(layoutInflater)
        accountBinding.addAccount.setOnClickListener {
            var intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(this.localClassName, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(this.localClassName, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(this.localClassName, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(this.localClassName, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(this.localClassName, "onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(this.localClassName, "onRestart called")
    }
}