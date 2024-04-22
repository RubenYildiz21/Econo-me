package be.helmo.projetmobile.view

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import be.helmo.projetmobile.R
import be.helmo.projetmobile.database.ItemsConverter
import be.helmo.projetmobile.databinding.FragmentAccountBinding
import be.helmo.projetmobile.model.Compte
import be.helmo.projetmobile.viewmodel.AccountListViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val accountViewModel: AccountListViewModel by viewModels()

    private var selectedIconBitmap: Bitmap? = null  // To hold the selected image URI

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAccount.setOnClickListener {
            showAddAccountDialog()
        }

        binding.btnTransfer.setOnClickListener {
            showAddTransferDialog()
        }
    }

    private fun showAddTransferDialog() {
        val dialogView = layoutInflater.inflate(R.layout.fragment_account_transfert, null)
        val sourceAccount = dialogView.findViewById<EditText>(R.id.sourceAccountSpinner)
        val destinationAccount = dialogView.findViewById<EditText>(R.id.destinationAccountSpinner)
        val amount = dialogView.findViewById<EditText>(R.id.transferAmount)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.addAccount))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val source = sourceAccount.text.toString()
                val destinationt = destinationAccount.text.toString()
                val amount = amount.text.toString().toDouble()
                lifecycleScope.launch {

                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }


    private fun showAddAccountDialog() {
        val dialogView = layoutInflater.inflate(R.layout.fragment_account_add, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.accountName)
        val currencyEditText = dialogView.findViewById<EditText>(R.id.accountCurrency)
        val balanceEditText = dialogView.findViewById<EditText>(R.id.accountBalance)


        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.addAccount))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val name = nameEditText.text.toString()
                val currency = currencyEditText.text.toString()
                val balance = balanceEditText.text.toString().toDouble()
                lifecycleScope.launch {
                    accountViewModel.addCompte(name, currency, balance)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun openImageSelector() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            val imageUri: Uri? = data?.data
            val imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
            selectedIconBitmap = imageBitmap
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid leaking views
    }

    private suspend fun getBitmap(imageUrl: String): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(imageUrl)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap

    }

    private suspend fun saveAccountData(name: String, currency: String, amount: Double) {
        val iconByteArray = ItemsConverter().fromBitmap(selectedIconBitmap!!)
        accountViewModel.addCompte(name, currency, amount)
    }
}
