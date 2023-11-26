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
            .whereEqualTo("id", "103454243208198857541")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (info in result) {
                    val reviewInfo = MyReviewListLayout(
                        info.id,
                        info.get("storeName") as String?,
                        info.get("score") as Int?,
                        info.get("review") as String?,
                        info.get("picture") as String?
                    )
                    count++
                    Log.d("storeName 나와라", info.get("storeName").toString())
                    Log.d("score 나와라", info.get("score").toString())
                    Log.d("review 나와라", info.get("review").toString())
                    Log.d("picture 나와라", info.get("picture").toString())
                    itemList.add(reviewInfo)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = MyReviewAdapter(itemList, this)
                binding.reviewCount.text = "내가 작성한 리뷰 총 ${count.toString()}개"
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
