package com.ssrdi.co.id.myradboox.fragmentreseller

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.google.android.material.snackbar.Snackbar
import com.ssrdi.co.id.myradboox.R
import com.ssrdi.co.id.myradboox.api.RadbooxApi
import com.ssrdi.co.id.myradboox.api.RetrofitClient
import com.ssrdi.co.id.myradboox.databinding.FragmentDetailVoucherBinding
import com.ssrdi.co.id.myradboox.model.VoucherResponse
import com.ssrdi.co.id.myradboox.storage.SharedPrefManager
import com.ssrdi.co.id.myradboox.utility.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail_voucher.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailVoucherFragment : Fragment() {
    val PERMISSION_BLUETOOTH = 1

    private val locale = Locale("id", "ID")
    private val df: DateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale)
    private val nf = NumberFormat.getCurrencyInstance(locale)
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentDetailVoucherBinding
    lateinit var retro: RadbooxApi

    lateinit var tokenLogin: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailVoucherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idDetail: Int = arguments?.getInt("voucherId") ?: 0

        // buat object retrofit
        retro = RetrofitClient.getInstance(requireContext())

        // panggil api voucher
        getDetailVoucher(idDetail)

        btn_print.setOnClickListener {
            requestPermissionBluetooth(view)
            Toast.makeText(requireContext(), "Btn Print", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getDetailVoucher(idDetail: Int) {

        tokenLogin = SharedPrefManager.getInstance(requireContext()).tokenLogin

        retro.getVoucherById("Bearer $tokenLogin", idDetail)
            .enqueue(object : Callback<VoucherResponse> {
                override fun onResponse(
                    call: Call<VoucherResponse>,
                    response: Response<VoucherResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("debug", "Respons->${data?.data}")

                        if (data != null) {
                            val detailVoucher = data.data[0]
                            txt_username1.text = detailVoucher.username
                            txt_Profile.text = detailVoucher.profile
                            txt_Status.text = detailVoucher.status
                            txt_CreateFirstTime.text = detailVoucher.create_time
                            txt_StartTime.text = detailVoucher.start_time
                            txt_EndTime.text = detailVoucher.end_time
                        }
                    } else {
                        Toast.makeText(
                            requireContext(), "Response gagal", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<VoucherResponse>, t: Throwable) {
                    Log.e("debug", "error -> ${t.localizedMessage}")
                }

            })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.i("Permission: ", "Granted")
            } else {
                Timber.i("Permission: ", "Denied")
            }
        }

    private fun requestPermissionBluetooth(view: View) {
        when {
            // check punya permission belum
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED -> {
                // sudah punya permission bluetooth
                doPrint()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.BLUETOOTH
            ) -> {
                binding.root.showSnackbar(
                    view,
                    "Perlu permission bluetooth",
                    Snackbar.LENGTH_INDEFINITE,
                    "OK"
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
                }
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
        }


    }

    private fun doPrint() {
        try {
            val connection = BluetoothPrintersConnections.selectFirstPaired()
            if (connection != null) {
                val printer = EscPosPrinter(connection, 203, 48f, 32)
                val text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
                    printer,
                    this.requireActivity()?.applicationContext?.resources
                        ?.getDrawableForDensity(
                            R.drawable.logo,
                            DisplayMetrics.DENSITY_LOW, context?.theme
                        )
                ) + "</img>\n" +
                        "[L]\n" +
                        "[L]" + df.format(Date()) + "\n" +
                        "[C]================================\n" +
                        "[L]<b>Effective Java</b>\n" +
                        "[L]    1 pcs[R]" + nf.format(25000) + "\n" +
                        "[L]<b>Headfirst Android Development</b>\n" +
                        "[L]    1 pcs[R]" + nf.format(45000) + "\n" +
                        "[L]<b>The Martian</b>\n" +
                        "[L]    1 pcs[R]" + nf.format(20000) + "\n" +
                        "[C]--------------------------------\n" +
                        "[L]TOTAL[R]" + nf.format(90000) + "\n" +
                        "[L]DISCOUNT 15%[R]" + nf.format(13500) + "\n" +
                        "[L]TAX 10%[R]" + nf.format(7650) + "\n" +
                        "[L]<b>GRAND TOTAL[R]" + nf.format(84150) + "</b>\n" +
                        "[C]--------------------------------\n" +
                        "[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
                        "[C]--------------------------------\n" +
                        "[C]Thanks For Shopping\n" +
                        "[C]https://kodejava.org\n" +
                        "[L]\n" +
                        "[L]<qrcode>https://kodejava.org</qrcode>\n"
                printer.printFormattedText(text)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No printer was connected!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Timber.e("APP", "Can't print", e)
        }

    }
}

fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}
