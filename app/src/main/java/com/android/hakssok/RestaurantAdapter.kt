package com.android.hakssok

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RestaurantAdapter(val restaurnatList: ArrayList<RestaurantListLayout>) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    private val itemList = arrayListOf<CouponListLayout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_recycler_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurnatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemList.clear()
        holder.name.text = restaurnatList[position].name
        holder.location.text = restaurnatList[position].location
        for (index in 0 until (restaurnatList[position].date.size)) {
            val couponInfo = CouponListLayout(
                restaurnatList[position].date.get(index),
                restaurnatList[position].content?.get(index),
                restaurnatList[position].college?.get(index)
            )
            itemList.add(couponInfo)
        }

        var listAdapter = CouponListAdapter(itemList)
        holder.coupon.layoutManager =
            LinearLayoutManager(holder.coupon.context, LinearLayoutManager.VERTICAL, false)
        holder.coupon.adapter = listAdapter

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("storeName", restaurnatList[position].name)
            intent.putExtra("storeId", restaurnatList[position].storeId)
            intent.putExtra("latitude", restaurnatList[position].latitude)
            intent.putExtra("longitude", restaurnatList[position].longitude)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.location)
        val coupon: RecyclerView = itemView.findViewById(R.id.coupon_recycler_view)
    }
}

class CouponListLayout(
    val date: String?,
    val coupon: String?,
    val college: String?,
)