package com.android.hakssok

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CouponListAdapter(val couponList: ArrayList<CouponListLayout>) :
    RecyclerView.Adapter<CouponListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.coupon_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = couponList[position].date
        holder.coupon.text = couponList[position].coupon
        holder.college.text = couponList[position].college
        couponList[position].college?.let { Log.d("goeun", it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val coupon: TextView = itemView.findViewById(R.id.coupon)
        val college: TextView = itemView.findViewById(R.id.college)
    }
}