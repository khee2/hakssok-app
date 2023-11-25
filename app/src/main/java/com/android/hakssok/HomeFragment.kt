package com.android.hakssok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private val db = Firebase.firestore
    private val itemList = arrayListOf<RestaurantListLayout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)


        val listAdapter = RestaurantAdapter(itemList)

        binding.myCollegeCouponRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.myCollegeCouponRecyclerView.adapter = listAdapter


//        val restaurant = db.collection("store")
//        restaurant.whereEqualTo("category", intent.getStringExtra("storeId").toString())
//            .get()
//            .addOnSuccessListener { result ->
//                itemList.clear()
//                for (info in result) {
//                    val restaurantInfo = RestaurantListLayout(
//                        info["storeName"] as String?,
//                        info.id,
//                        info["location"] as String?,
//                        info.get("date") as ArrayList<String>,
//                        info.get("content") as ArrayList<String>,
//                        info.get("college") as ArrayList<String>,
//                    )
//                    itemList.add(restaurantInfo)
//                }
//                listAdapter.notifyDataSetChanged()
//            }
        return binding.root
    }
}