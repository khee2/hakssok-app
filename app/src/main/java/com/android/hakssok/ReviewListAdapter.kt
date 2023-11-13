package com.android.hakssok

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ReviewListAdapter(val reviewList: ArrayList<ReviewListLayout>) :
    RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.review_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = reviewList[position].username
        holder.date.text = reviewList[position].date.toString()
        holder.review.text = reviewList[position].review
        // TODO 스코어, 사진 추가

        // TODO 플로어 버튼 누르면 리뷰 화면으로 전환
        // 가게 이름 넘기기
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.username)
        val date: TextView = itemView.findViewById(R.id.date)
        val review: TextView = itemView.findViewById(R.id.review)
    }
}