package com.android.hakssok

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.ListPageBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

class CollegeActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val itemList = arrayListOf<ListLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        val categorySpinner: Spinner = findViewById(R.id.category_spinner)
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        val categoryList = resources.getStringArray(R.array.college)

        toolbarTitle.text = resources.getString(R.string.college)
        categorySpinner.dropDownWidth = 400

        val adapter = SpinnerAdapter(this, categoryList)
        categorySpinner.adapter = adapter

        val listAdapter = ListAdapter(itemList)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
                restaurant.whereEqualTo("college", categoryList[num])
                    .get()
                    .addOnSuccessListener { result ->
                        itemList.clear()
                        for (info in result) {
                            val restaurantInfo = ListLayout(
                                info["storeName"] as String?,
                                info["location"] as String?,
                                info["date"] as String?,
                                info["content"] as String?,
                                null
                            )
                            itemList.add(restaurantInfo)
                        }
                        listAdapter.notifyDataSetChanged()
                    }
            }
        }
    }
}