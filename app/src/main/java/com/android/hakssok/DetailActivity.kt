package com.android.hakssok

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.DetailPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private lateinit var binding: DetailPageBinding
    private lateinit var mapFragment: SupportMapFragment
    private var isFabOpen = false

    private val db = Firebase.firestore
    private val reviewList = arrayListOf<ReviewListLayout>()
    private val couponList = arrayListOf<CouponListLayout>()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recyclerview
        val reviewAdapter = ReviewListAdapter(reviewList)
        val couponAdapter = CouponListAdapter(couponList)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = reviewAdapter

        binding.couponRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.couponRecyclerView.adapter = couponAdapter

        latitude = intent.getStringExtra("latitude").toString().toDouble()
        longitude = intent.getStringExtra("longitude").toString().toDouble()

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val restaurant = db.collection("store")
        restaurant
            .document(intent.getStringExtra("storeId").toString())
            .get()
            .addOnSuccessListener { result ->
                couponList.clear()
                val dateList = result.get("date") as ArrayList<*>
                val contentList = result.get("content") as ArrayList<*>
                val collegeList = result.get("college") as ArrayList<*>

                binding.name.text = result["storeName"] as String?
                binding.location.text = result["location"] as String?
                binding.phone.text = result["phone"] as String?

                for (index in 0 until dateList.size) {
                    val coupon = CouponListLayout(
                        dateList[index] as String?,
                        contentList[index] as String?,
                        collegeList[index] as String?
                    )
                    couponList.add(coupon)
                }
                couponAdapter.notifyDataSetChanged()
            }

        val user = db.collection("user")
        user.whereEqualTo("storeId", intent.getStringExtra("storeId"))
            .get()
            .addOnSuccessListener { result ->
                // TODO 사진 수정
                for (review in result) {
                    val review = ReviewListLayout(
                        review["username"] as String?,
                        review["date"] as Timestamp,
                        review["review"] as String?,
                        review["score"] as Number?,
                        review["picture"] as String?
                    )
                    reviewList.add(review)
                }
                reviewAdapter.notifyDataSetChanged()
            }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    changeFragment(HomeFragment())
                }
                R.id.fragment_menu -> {
                    changeFragment(RestaurantFragment())
                }
                R.id.fragment_coupon -> {
                    changeFragment(CollegeFragment())
                }
            }
            true
        }

        // ****여기 부분 새로 추가함.****
        binding.registerBtn.setOnClickListener{
            val MyIntent = Intent(this, RegisterActivity::class.java)
            MyIntent.putExtra("storeId",intent.getStringExtra("storeId"))
            MyIntent.putExtra("storeName",intent.getStringExtra("storeName"))
            startActivity(MyIntent)
        }
    }
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        val latLong = LatLng(latitude, longitude)
        googleMap!!.clear()
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLong))
        googleMap!!.addMarker(MarkerOptions().position(latLong).title("여기"))
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 20f))
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContent.id, fragment).commit()
    }
}

class ReviewListLayout(
    val username: String?,
    val date: Timestamp,
    val review: String?,
    val score: Number?,
    val picture: String?
)