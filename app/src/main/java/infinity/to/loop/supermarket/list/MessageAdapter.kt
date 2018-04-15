package infinity.to.loop.supermarket.list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import infinity.to.loop.supermarket.R
import infinity.to.loop.supermarket.list.MessageAdapter.MessageHolder
import infinity.to.loop.supermarket.models.Message


class MessageAdapter(private val listener: ItemChoice) : RecyclerView.Adapter<MessageHolder>() {

    private val itemsList = ArrayList<Message>()
    private var pressAllowed: Boolean = false

    fun update(message: Message) {
        itemsList.add(0, message)
        notifyItemInserted(0)
    }

    fun allowPress(allow: Boolean) {
        this.pressAllowed = allow
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = itemsList[position]
        holder.name?.text = message.name
        holder.weight?.text = message.weight

        val drawable = holder.bagColor?.background as GradientDrawable
        val color = Color.parseColor(message.bagColor)
        drawable.setColor(color)

        if (pressAllowed) {
            holder.itemView?.setOnClickListener({ view -> listener.onItemChoosen(color) })
        }
    }

    override fun getItemCount() = itemsList.size

    class MessageHolder(itemView: View) : ViewHolder(itemView) {

        val name: TextView? = itemView.findViewById(R.id.item_name)
        val bagColor: View? = itemView.findViewById(R.id.item_color)
        val weight: TextView? = itemView.findViewById(R.id.item_info)
    }
}