package jp.gr.java_conf.foobar.remainder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationService(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotification(memo: RemindMemo) {

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("id", memo.id)
        val contentIntent = PendingIntent.getActivity(context, memo.id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val name = context.getString(R.string.app_name)
        val id = context.getString(R.string.channel_id)
        val notifyDescription = context.getString(R.string.notify_description)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager.getNotificationChannel(id) == null) {
                val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                mChannel.description = notifyDescription
                notificationManager.createNotificationChannel(mChannel)
            }
        }

        val notification = NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setTicker(memo.title)
                .setContentTitle(memo.title)
                .setContentIntent(contentIntent)
                .setDefaults(
                        Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .build()

        notification.flags = Notification.FLAG_ONGOING_EVENT
        notificationManager.notify(memo.id, notification)

    }

    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

}