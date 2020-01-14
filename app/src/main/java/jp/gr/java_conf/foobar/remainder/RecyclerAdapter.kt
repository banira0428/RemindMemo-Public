package jp.gr.java_conf.foobar.remainder

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(context: Context, private val listener: Listener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    var remindMemoList = emptyList<RemindMemo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface Listener {
        fun onClickContent(remindMemo: RemindMemo)
        fun onClickToggleRemind(remindMemo: RemindMemo)
        fun onClickDelete(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.card_memo, parent, false))
    }

    override fun getItemCount(): Int {
        return remindMemoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = remindMemoList[holder.adapterPosition]

        holder.memo.text = data.title

        holder.itemView.setOnClickListener {
            listener.onClickContent(data)
        }

        holder.remind.apply {
            setImage(if (data.isRemind) R.drawable.ic_notifications_off_white_24dp else R.drawable.ic_notifications_black_24dp)
            setText(context.getString(if (data.isRemind) R.string.cancel_notify else R.string.notify))
            setOnClickListener {
                listener.onClickToggleRemind(data)
            }
        }

        holder.delete.apply {
            setImage(R.drawable.ic_delete_black_24dp)
            setText(context.getString(R.string.action_delete))
            setOnClickListener {
                AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(context.getString(R.string.action_delete_memo))
                .setMessage(context.getString(R.string.msg_delete_memo, data.title))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listener.onClickDelete(data.id)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create().show()
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var memo: TextView = v.findViewById<View>(R.id.text_memo) as TextView
        var remind: ImageTextButton = v.findViewById<View>(R.id.button_remind) as ImageTextButton
        var delete: ImageTextButton = v.findViewById<View>(R.id.button_delete) as ImageTextButton
    }


}