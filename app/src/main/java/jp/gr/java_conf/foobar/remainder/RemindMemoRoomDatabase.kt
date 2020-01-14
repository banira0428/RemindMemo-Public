package jp.gr.java_conf.foobar.remainder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RemindMemo::class, History::class], version = 1, exportSchema = false)
abstract class RemindMemoRoomDatabase : RoomDatabase() {

    abstract fun remindMemoDao(): RemindMemoDao

    companion object {

        @Volatile
        private var INSTANCE: RemindMemoRoomDatabase? = null

        fun getDatabase(context: Context): RemindMemoRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        RemindMemoRoomDatabase::class.java,
                        "remind_memo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}