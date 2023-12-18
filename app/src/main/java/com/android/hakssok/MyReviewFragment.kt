package com.android.hakssok

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.FragmentMyReviewBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MyReviewFragment : Fragment(), MyReviewAdapterListener {

    private val db = Firebase.firestore
    private val itemList = arrayListOf<MyReviewListLayout>()
    private lateinit var rootView: View  // 저장할 View 변수 추가

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding = FragmentMyReviewBinding.inflate(inflater, container, false)
        rootView = binding.root
        db.collection("review")
            .whereEqualTo("id", LoginApp.id)
            .get()
            .addOnSuccessListener { result ->
                for (info in result) {
                    val reviewInfo = MyReviewListLayout(
                        info.id,
                        info.getString("storeName"),
                        info.getLong("score")?.toInt(),
                        info.getString("review"),
                        info.getString("picture")
                    )
                    itemList.add(reviewInfo)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = MyReviewAdapter(itemList, this)
                val count = itemList.size
                binding.reviewCount.text = "내가 작성한 리뷰 총 ${count.toString()}개"
            }
            .addOnCompleteListener {
                if (itemList.isEmpty()) {
                    binding.noContentText.visibility = View.VISIBLE
                } else {
                    binding.noContentText.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                Log.d("실패","맞는 Id가 없음")
            }
        return rootView
    }

    override fun onReviewDeleted() {
        // 리뷰가 삭제되었을 때 호출되는 메서드
        val count = itemList.size
        rootView.findViewById<TextView>(R.id.review_count).text = "내가 작성한 리뷰 총 ${count.toString()}개"
    }
}

class MyReviewListLayout(
    val documentId: String?,
    val storeName: String?,
    val score: Int?,
    val review: String?,
    val picture: String?
)
