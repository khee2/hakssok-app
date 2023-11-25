package com.android.hakssok

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.hakssok.databinding.FragmentMyReviewBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MyReviewFragment : Fragment() {

    private val db = Firebase.firestore
    private val itemList = arrayListOf<MyReviewListLayout>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyReviewBinding.inflate(inflater, container, false)

        db.collection("review")
            .whereEqualTo("id", "103454243208198857541")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (info in result) {
                    val reviewInfo = MyReviewListLayout(
                        info.id,
                        info.get("storeName") as String?,
                        info.get("score") as String?,
                        info.get("review") as String?,
                        info.get("picture") as String?)
                    count++
                    Log.d("storeName 나와라", info.get("storeName").toString())
                    Log.d("score 나와라", info.get("score").toString())
                    Log.d("review 나와라", info.get("review").toString())
                    Log.d("picture 나와라", info.get("picture").toString())
                    itemList.add(reviewInfo)
                }
                binding.reviewCount.text = "내가 작성한 리뷰 총 ${count.toString()}개"
            }
        // 리사이클러 뷰에 어댑터와 레이아웃 매니저 등록
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = MyReviewAdapter(itemList)
        return binding.root
    }
}

class MyReviewListLayout(
    val documentId: String?,
    val storeName: String?,
    val score: String?,
    val review: String?,
    val picture: String?
)
