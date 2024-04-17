package be.helmo.projetmobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import be.helmo.projetmobile.databinding.AccountsActivityBinding

class AccountActivity : ComponentActivity() {

    private lateinit var binding: AccountsActivityBinding
    private val accountsList = listOf(
        Account("Compte Chèque", "Description", "EUR", 1000.0),
        Account("Épargne", "Description", "EUR", 2000.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.localClassName, "onCreate called")

        val binding = AccountsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAccount.setOnClickListener {
            showAddAccountDialog()
        }

        binding.AddExpense.setOnClickListener {
            var intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        binding.btnTransfer.setOnClickListener {
            showTransferDialog()
        }

    }

    private fun showTransferDialog() {
        val dialogView = layoutInflater.inflate(R.layout.transfer_dialog, null)
        val sourceAccountSpinner = dialogView.findViewById<Spinner>(R.id.sourceAccountSpinner)
        val destinationAccountSpinner = dialogView.findViewById<Spinner>(R.id.destinationAccountSpinner)
        val transferAmountEditText = dialogView.findViewById<EditText>(R.id.transferAmount)

        val accountNames = accountsList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accountNames)
        sourceAccountSpinner.adapter = adapter
        destinationAccountSpinner.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.transfer))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.transfer)) { dialog, which ->
                val sourceAccount = accountsList[sourceAccountSpinner.selectedItemPosition]
                val destinationAccount = accountsList[destinationAccountSpinner.selectedItemPosition]
                val amount = transferAmountEditText.text.toString().toDouble()

            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    private fun showAddAccountDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_account_dialog, null)
        val editText = dialogView.findViewById<EditText>(R.id.transferAmount)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.transfer))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.transfer)) { dialog, which ->
                val amount = editText.text.toString().toDouble()
                // Effectuez le transfert ici
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }


}