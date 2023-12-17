package com.android.hakssok

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.lang.String.format
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReviewListAdapter(val reviewList: ArrayList<ReviewListLayout>) :
    RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = reviewList[position].username
        val time = reviewList[position].date.toDate()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val str = format.format(time)
        holder.date.text = str
        holder.review.text = reviewList[position].review
        holder.ratingBar.rating = reviewList[position].score.toString().toFloat()
        Glide.with(holder.itemView.context).load(reviewList[position].picture)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20))) // 사진 테두리
            .into(holder.picture)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.username)
        val date: TextView = itemView.findViewById(R.id.date)
        val review: TextView = itemView.findViewById(R.id.review)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val picture: ImageView = itemView.findViewById(R.id.reviewImage)
    }
}