package com.android.hakssok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val itemList: ArrayList<ListLayout>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.date.text = itemList[position].date
        holder.content.text = itemList[position].content
        holder.location.text = itemList[position].location
        val college = itemList[position].college
        if (college != null) {
            holder.college.text = college
        } else {
            holder.college.visibility = View.GONE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val content: TextView = itemView.findViewById(R.id.coupon)
        val college: TextView = itemView.findViewById(R.id.college)
        val location: TextView = itemView.findViewById(R.id.location)
    }
}