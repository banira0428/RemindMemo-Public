package jp.gr.java_conf.foobar.remainder

import androidx.lifecycle.LiveData

class RemindMemoRepository(private val dao: RemindMemoDao) {

    val remindMemoList: LiveData<List<RemindMemo>> = dao.getRemindMemoList()
    val historyList: LiveData<List<History>> = dao.getHistoryList()

    suspend fun createRemindMemo(remindMemo: RemindMemo) {
        dao.insert(remindMemo)
        dao.insert(History(remindMemo.title))
    }

    suspend fun updateRemindMemo(remindMemo: RemindMemo) {
        dao.updateTitle(remindMemo.id, remindMemo.title)
        dao.insert(History(remindMemo.title))
    }

    suspend fun deleteRemindMemo(id: Int) {
        dao.deleteRemindMemo(id)
    }

    suspend fun turnOnRemind(id: Int) {
        dao.updateIsRemind(id, true)
    }

    suspend fun turnOffRemind(id: Int) {
        dao.updateIsRemind(id, false)
    }

    suspend fun clearHistory() {
        dao.deleteHistory()
    }

}