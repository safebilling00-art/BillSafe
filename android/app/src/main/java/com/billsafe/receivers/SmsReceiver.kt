package com.billsafe.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.billsafe.data.database.AppDatabase
import com.billsafe.utils.SmsParser
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SmsReceiverEntryPoint {
    fun database(): AppDatabase
}

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val database = EntryPointAccessors.fromApplication(
            context.applicationContext,
            SmsReceiverEntryPoint::class.java
        ).database()

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (message in messages) {
            val smsBody = message.displayMessageBody

            val bill = SmsParser.parseSms(smsBody) ?: continue

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    database.billDao().insertBill(bill)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
