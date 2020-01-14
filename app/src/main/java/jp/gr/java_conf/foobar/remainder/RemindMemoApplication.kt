package jp.gr.java_conf.foobar.remainder

import android.app.Application
import org.koin.android.ext.android.startKoin

/**
 * Created by keita on 2016/07/17.
 */
class RemindMemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val dao = RemindMemoRoomDatabase.getDatabase(applicationContext).remindMemoDao()

        startKoin(this, listOf(
                getRemindMemoModules(dao)
        ))
    }
}