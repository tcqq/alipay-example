package com.example.alipay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.alipay.sdk.app.EnvUtils
import com.example.alipay.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.payment.setOnClickListener {
            if (binding.orderInfo.text.isNullOrBlank()) {
                showToast("Please enter the order info")
            } else {
                val alipayUtil = AlipayUtil(this)
                val orderInfo = binding.orderInfo.text.toString()
                lifecycleScope.launch {
                    alipayUtil.pay(orderInfo) { payResult ->
                        val resultStatus = payResult.resultStatus
                        when(payResult.resultStatus) {
                            PAYMENT_SUCCESSFUL -> showToast("Payment successful ($resultStatus)")
                            PAYMENT_PROCESSING -> showToast("Payment processing ($resultStatus)")
                            PAYMENT_CANCELED -> showToast("Payment canceled ($resultStatus)")
                            else -> showToast("Payment failed ($resultStatus)")
                        }
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PAYMENT_SUCCESSFUL = "9000"
        private const val PAYMENT_PROCESSING = "8000"
        private const val PAYMENT_CANCELED = "6001"
    }
}
