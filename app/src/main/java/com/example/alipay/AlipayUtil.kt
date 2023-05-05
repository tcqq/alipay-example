package com.example.alipay

import android.app.Activity
import com.alipay.sdk.app.PayTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Perry Lance
 * @since 2023-04-30 Created
 */
class AlipayUtil(private val activity: Activity) {

    suspend fun pay(orderInfo: String, callback: (PayResult) -> Unit) {
        val payResult = withContext(Dispatchers.IO) {
            val payTask = PayTask(activity)
            val result = payTask.payV2(orderInfo, true)
            parseResult(result)
        }
        callback(payResult)
    }

    private fun parseResult(result: Map<String, String>): PayResult {
        return PayResult(
            result["resultStatus"] ?: "",
            result["result"] ?: "",
            result["memo"] ?: ""
        )
    }
}
