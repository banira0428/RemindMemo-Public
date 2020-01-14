package jp.gr.java_conf.foobar.remainder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(@PrimaryKey @ColumnInfo(name = "content") val content: String)