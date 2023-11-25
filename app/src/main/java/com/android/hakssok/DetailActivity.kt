package com.android.hakssok

import android.os.Bundle
import android.text.Layout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    private val reviewList = arrayListOf<ReviewListLayout>()
    private val couponList = arrayListOf<CouponListLayout>()

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

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        suwon1 = new LatLng(37.287617, 127.018057);
//        mMap.addMarker(new MarkerOptions().position(suwon1).title("방화수령전"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(suwon1));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(suwon1,14));//지도를 14배율로 확대해서 보여줌

        // TODO 하단 탭 눌리게 하기

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
                // TODO TimeStamp 수정 필요
                // score 라이브러리 사용하기
                // 사진 저장 방법 찾아보기
                // 구글 지도 마커 생성하기
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