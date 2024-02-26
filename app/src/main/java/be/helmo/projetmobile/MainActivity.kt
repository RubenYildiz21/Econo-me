package be.helmo.projetmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.localClassName, "onCreate called")
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