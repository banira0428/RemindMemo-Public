package jp.gr.java_conf.foobar.remainder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "remind_memo_table")
data class RemindMemo(@PrimaryKey @ColumnInfo(name = "id") val id: Int = Date().time.toInt(),
                      @ColumnInfo(name = "title") val title: String,
                      @ColumnInfo(name = "is_remind") val isRemind: Boolean = true,
                      @ColumnInfo(name = "date_created") val date: String = Date().toString()) {


    companion object {
        fun replaceTitle(remindMemo: RemindMemo, title: String): RemindMemo =
                RemindMemo(remindMemo.id, title, remindMemo.isRemind, remindMemo.date)
    }
}