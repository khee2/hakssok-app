package com.android.hakssok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        //TODO 사진 추가
        holder.ratingBar.rating = reviewList[position].score.toString().toFloat()

        //TODO 플로어 버튼 누르면 리뷰 화면으로 전환
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.username)
        val date: TextView = itemView.findViewById(R.id.date)
        val review: TextView = itemView.findViewById(R.id.review)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }
}