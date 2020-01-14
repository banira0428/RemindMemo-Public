package jp.gr.java_conf.foobar.remainder

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemindMemoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(remindMemo: RemindMemo)

    @Query("SELECT * from remind_memo_table ORDER BY date_created ASC")
    fun getRemindMemoList(): LiveData<List<RemindMemo>>

    @Query("SELECT * from remind_memo_table ORDER BY date_created ASC")
    fun getRemindMemoListFromBackground(): List<RemindMemo>

    @Query("DELETE FROM remind_memo_table WHERE id = :id")
    suspend fun deleteRemindMemo(id: Int)

    @Query("UPDATE remind_memo_table SET is_remind = :isRemind WHERE id = :id")
    suspend fun updateIsRemind(id: Int, isRemind: Boolean)

    @Query("UPDATE remind_memo_table SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: Int, title: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    @Query("SELECT * from history_table")
    fun getHistoryList(): LiveData<List<History>>

    @Query("DELETE FROM history_table")
    suspend fun deleteHistory()

}