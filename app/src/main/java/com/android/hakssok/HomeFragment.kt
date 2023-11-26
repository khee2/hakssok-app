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
    private val itemList = arrayListOf<RestaurantListForCollegeSearch>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val listAdapter = ListAdapter(itemList)

        binding.myCollegeCouponRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.myCollegeCouponRecyclerView.adapter = listAdapter

        // TODO searchKeyword intent로 수정
        val searchKeyword = "공과대학"

        val restaurant = db.collection("store")
        restaurant.whereArrayContains("college", searchKeyword)
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (info in result) {
                    val collegeArray = info.get("college") as ArrayList<*>
                    val index = collegeArray.indexOf(searchKeyword)
                    val restaurantInfo = RestaurantListForCollegeSearch(
                        info["storeName"] as String?,
                        info.id,
                        info["location"] as String?,
                        info.get("date") as ArrayList<String>,
                        info.get("content") as ArrayList<String>,
                        info.get("college") as ArrayList<String>,
                        info.get("latitude") as String?,
                        info.get("longitude") as String?,
                        index
                    )
                    itemList.add(restaurantInfo)
                }
                listAdapter.notifyDataSetChanged()
            }
            .addOnCompleteListener {
                if (itemList.isEmpty()) {
                    binding.noContentText.visibility = View.VISIBLE
                } else {
                    binding.noContentText.visibility = View.GONE
                }
            }

        return binding.root
    }
}