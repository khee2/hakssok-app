package com.android.hakssok

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.ListPageBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    val restaurant = db.collection("store")
    private val itemList = arrayListOf<ListLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        val categorySpinner: Spinner = findViewById(R.id.category_spinner)
        val restaurantList: Array<String> = arrayOf("양식", "한식", "일식", "중식", "카페/주류")

        val adapter = SpinnerAdapter(this, restaurantList)
        categorySpinner.adapter = adapter

        val listAdapter = ListAdapter(itemList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = listAdapter

        categorySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        restaurant.whereEqualTo("category", "양식")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (info in result) {
                                    val restaurantInfo = ListLayout(info["storeName"] as String?, info["location"] as String?, info["date"] as String?, info["content"] as String?)
                                    itemList.add(restaurantInfo)
                                }
                                listAdapter.notifyDataSetChanged()
                            }
                    }
                    1 -> {
                        restaurant.whereEqualTo("category", "한식")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (info in result) {
                                    val restaurantInfo = ListLayout(info["storeName"] as String?, info["location"] as String?, info["date"] as String?, info["content"] as String?)
                                    itemList.add(restaurantInfo)
                                }
                                listAdapter.notifyDataSetChanged()
                            }
                    }
                    2 -> {
                        restaurant.whereEqualTo("category", "일식")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (info in result) {
                                    val restaurantInfo = ListLayout(info["storeName"] as String?, info["location"] as String?, info["date"] as String?, info["content"] as String?)
                                    itemList.add(restaurantInfo)

                                }
                                listAdapter.notifyDataSetChanged()
                            }
                    }
                    3 -> {
                        restaurant.whereEqualTo("category", "중식")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (info in result) {
                                    val restaurantInfo = ListLayout(info["storeName"] as String?, info["location"] as String?, info["date"] as String?, info["content"] as String?)
                                    itemList.add(restaurantInfo)

                                }
                                listAdapter.notifyDataSetChanged()
                            }
                    }
                    4 -> {
                        restaurant.whereEqualTo("category", "카페/주류")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (info in result) {
                                    val restaurantInfo = ListLayout(info["storeName"] as String?, info["location"] as String?, info["date"] as String?, info["content"] as String?)
                                    itemList.add(restaurantInfo)
                                }
                                listAdapter.notifyDataSetChanged()
                            }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}

class ListLayout(val name: String?, val location: String?, val date: String?, val content: String?)