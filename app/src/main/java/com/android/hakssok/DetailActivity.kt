package com.android.hakssok

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.DetailPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private lateinit var binding: DetailPageBinding

    private val db = Firebase.firestore
    private val itemList = arrayListOf<ReviewListLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listAdapter = ReviewListAdapter(itemList)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = listAdapter

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val restaurant = db.collection("store")
        restaurant.limit(1)
            .whereEqualTo("storeName", intent.getStringExtra("storeName"))
            .get()
            .addOnSuccessListener { result ->
                for (info in result) {
                    binding.name.text = info["storeName"] as String?
                    binding.date.text = info["date"] as String?
                    binding.college.text = info["college"] as String?
                    binding.coupon.text = info["content"] as String?

                    // TODO 문의 전화 넣기
                }
            }


        val user = db.collection("user")
        user.whereEqualTo("storeId", intent.getStringExtra("storeId"))
            .get()
            .addOnSuccessListener { result ->
                // TODO TimeStamp 수정 필요
                for (review in result) {
                    val review = ReviewListLayout(
                        review["username"] as String?,
                        review["date"] as Timestamp,
                        review["review"] as String?,
                        review["score"] as Number?,
                        review["picture"] as String?
                    )

                    itemList.add(review)
                }
                listAdapter.notifyDataSetChanged()
            }

    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        val lating = LatLng(37.9, 126.7)
        val position = CameraPosition.builder()
            .target(lating)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

    }
}

class ReviewListLayout(
    val username: String?,
    val date: Timestamp,
    val review: String?,
    val score: Number?,
    val picture: String?
)