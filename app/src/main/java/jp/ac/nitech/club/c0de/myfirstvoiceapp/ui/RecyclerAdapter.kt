package jp.ac.nitech.club.c0de.myfirstvoiceapp.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.ac.nitech.club.c0de.myfirstvoiceapp.R

/**
 *
 * @author t_kamiya
 * Created on 2018/10/05
 */
class RecyclerAdapter(
        context: Context,
        val data: MutableList<ChatItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    companion object {
        const val TYPE_USER = 10
        const val TYPE_BOT = 20
        const val TYPE_SPACE = 30
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) {
            TYPE_SPACE
        } else if (data[position].isMine) {
            TYPE_USER
        } else {
            TYPE_BOT
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER -> ViewHolder(inflater.inflate(R.layout.list_item_user, viewGroup, false));
            TYPE_BOT -> ViewHolder(inflater.inflate(R.layout.list_item_bot, viewGroup, false));
            TYPE_SPACE -> EmptyHolder(inflater.inflate(R.layout.list_space, viewGroup, false))
            else -> throw IllegalArgumentException("unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_SPACE -> {

            }
            TYPE_USER, TYPE_BOT -> {
                (viewHolder as ViewHolder).textView.text = data[position].message
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size+1
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv_chat) // 今回は名前を共通化して使いまわし
    }

    class EmptyHolder(view: View): RecyclerView.ViewHolder(view)
}