package com.android.hakssok

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val itemList: ArrayList<RestaurantListForCollegeSearch>) :
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
        val index = itemList[position].index
        holder.name.text = itemList[position].name
        holder.location.text = itemList[position].location
        holder.date.text = itemList[position].date.get(index)
        holder.content.text = itemList[position].content?.get(index)

        val college = itemList[position].college?.get(index)
        val noShowCollege = itemList[position].notShowCollege
        if (noShowCollege) {
            holder.college.visibility = View.GONE
        } else {
            holder.college.text = college
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("storeName", itemList[position].name)
            intent.putExtra("storeId", itemList[position].storeId)
            intent.putExtra("latitude", itemList[position].latitude)
            intent.putExtra("longitude", itemList[position].longitude)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
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