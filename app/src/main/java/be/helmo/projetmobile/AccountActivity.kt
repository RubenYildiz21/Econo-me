package be.helmo.projetmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import be.helmo.projetmobile.databinding.AccountsActivityBinding

class AccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.localClassName, "onCreate called")

        val binding = AccountsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAccount.setOnClickListener {
            var intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

    }
}