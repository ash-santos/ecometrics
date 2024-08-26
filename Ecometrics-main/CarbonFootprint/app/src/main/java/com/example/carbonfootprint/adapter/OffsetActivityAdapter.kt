package com.example.carbonfootprint.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carbonfootprint.R


data class DataOffsetModel(
    val column1Data: String,
    val column2Data: String,
    val column3Data: String
)


@Suppress("DEPRECATION")
class OffsetActivityAdapter(
    private val dataList: List<DataOffsetModel>,
    private val listener: OnItemClickListener,
    private var context: Context
) : RecyclerView.Adapter<OffsetActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_offset_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]

        val html = "<a href=\"${currentItem.column3Data}\">${currentItem.column1Data}</a>"
        holder.column1.text = Html.fromHtml(html)
        Linkify.addLinks(holder.column1, Linkify.WEB_URLS)
        holder.column2.text = currentItem.column2Data
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val column1: TextView = itemView.findViewById(R.id.link1)
        val column2: TextView = itemView.findViewById(R.id.link2)

        init {
            itemView.setOnClickListener(this)
            column1.setOnClickListener {
                val html = dataList[adapterPosition]
                html?.let {
                    val uri = Uri.parse(it.column3Data)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Add this line
                    context.startActivity(intent)
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}