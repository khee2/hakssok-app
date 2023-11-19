package com.android.hakssok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.FragmentListPageBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RestaurantFragment : Fragment() {
    private val db = Firebase.firestore
    private val itemList = arrayListOf<RestaurantListLayout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentListPageBinding.inflate(inflater, container, false)

        val categorySpinner: Spinner = binding.categorySpinner
        val toolbarTitle: TextView = binding.toolbarTitle
        val categoryList = resources.getStringArray(R.array.category)

        toolbarTitle.text = resources.getString(R.string.category)

        val adapter = SpinnerAdapter(inflater.context, categoryList)
        categorySpinner.adapter = adapter

        val listAdapter = RestaurantAdapter(itemList)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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
                    position -> getRestaurantInfo(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            fun getRestaurantInfo(num: Int) {
                val restaurant = db.collection("store")
                restaurant.whereEqualTo("category", categoryList[num])
                    .get()
                    .addOnSuccessListener { result ->
                        itemList.clear()
                        for (info in result) {
                            val restaurantInfo = RestaurantListLayout(
                                info["storeName"] as String?,
                                info.id,
                                info["location"] as String?,
                                info.get("date") as ArrayList<String>,
                                info.get("content") as ArrayList<String>,
                                info.get("college") as ArrayList<String>,
                            )
                            itemList.add(restaurantInfo)
                        }
                        listAdapter.notifyDataSetChanged()
                    }
            }
        }
        return binding.root
    }
}

class RestaurantListLayout(
    val name: String?,
    val storeId: String?,
    val location: String?,
    val date: ArrayList<String>,
    val content: ArrayList<String>?,
    val college: ArrayList<String>?,
)

