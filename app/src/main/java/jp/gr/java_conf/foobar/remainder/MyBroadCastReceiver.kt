package jp.gr.java_conf.foobar.remainder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {

                val notificationService = NotificationService(context)

                val dao = RemindMemoRoomDatabase.getDatabase(context).remindMemoDao()

                runBlocking {

                    GlobalScope.launch(Dispatchers.Default) {

                        dao.getRemindMemoListFromBackground().filter { it.isRemind }.forEach {
                            notificationService.createNotification(it)
                        }
                    }
                }
            }

        }
    }

}