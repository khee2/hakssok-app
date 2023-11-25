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
    private val itemList = arrayListOf<ListLayout>()

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
                    val restaurantInfo = ListLayout(
                        info["storeName"] as String?,
                        info["location"] as String?,
                        (info.get("date") as ArrayList<*>)[index] as String?,
                        (info.get("content") as ArrayList<*>)[index] as String?,
                        null,
                        info.id
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