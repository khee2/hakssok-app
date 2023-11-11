package com.android.hakssok

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SpinnerAdapter(private val context: Context, private val categoryList: Array<String>): BaseAdapter() {

    override fun getCount(): Int {
        return categoryList.size
    }

    override fun getItem(position: Int): Any {
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, viewGroup, false)

        val category: TextView = rootView.findViewById(R.id.category_text)
        category.text = categoryList[position]

        return rootView
    }
}