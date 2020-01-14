package jp.gr.java_conf.foobar.remainder

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.gr.java_conf.foobar.remainder.legacy.Memo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val notificationService: NotificationService, private val repository: RemindMemoRepository) : ViewModel() {

    var textMemo: MutableLiveData<String> = MutableLiveData()

    val remindMemoList: LiveData<List<RemindMemo>> = repository.remindMemoList

    val historyList: LiveData<List<History>> = repository.historyList

    fun onClickAdd(view: View) {
        GlobalScope.launch {
            textMemo.value?.let {

                val remindMemo = RemindMemo(title = it)
                repository.createRemindMemo(remindMemo)
                notificationService.createNotification(remindMemo)

                withContext(Dispatchers.Main) {
                    textMemo.value = ""
                }
            }
        }
    }

    fun onClickUpdate(remindMemo: RemindMemo){
        GlobalScope.launch {
            repository.updateRemindMemo(remindMemo)
            if(remindMemo.isRemind){
                notificationService.createNotification(remindMemo)
            }
        }
    }

    fun onClickDelete(id: Int) {
        GlobalScope.launch {
            repository.deleteRemindMemo(id)
            notificationService.cancelNotification(id)
        }
    }

    fun onClickToggleRemind(remindMemo: RemindMemo) {

        if (remindMemo.isRemind) {
            GlobalScope.launch {
                repository.turnOffRemind(remindMemo.id)
                notificationService.cancelNotification(remindMemo.id)
            }
        } else {
            GlobalScope.launch {
                repository.turnOnRemind(remindMemo.id)
                notificationService.createNotification(remindMemo)
            }
        }
    }

    fun convertToRoom(memoList: ArrayList<Memo>) {

        GlobalScope.launch {
            memoList.forEach {
                val remindMemo = RemindMemo(title = it.memo,isRemind = it.remind)
                repository.createRemindMemo(remindMemo)

                if(it.remind){
                    notificationService.createNotification(remindMemo)
                }
            }
        }

    }

    fun clearHistory() {

        GlobalScope.launch {
            repository.clearHistory()
        }
    }

    fun onClickImport(text: String) {

        GlobalScope.launch {
            text.split("\n").forEach {

                val remindMemo = RemindMemo(title = it)
                repository.createRemindMemo(remindMemo)
                notificationService.createNotification(remindMemo)

            }
        }
    }
}