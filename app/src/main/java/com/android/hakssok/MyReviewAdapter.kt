package com.android.hakssok

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.hakssok.databinding.MyReviewListBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyReviewAdapter(
    private val myReviewList: ArrayList<MyReviewListLayout>,
    private val listener: MyReviewAdapterListener
) :
    RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return myReviewList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MyReviewListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val db = Firebase.firestore
        val binding = holder.binding
        binding.storeName.text = myReviewList[position].storeName
        val score = myReviewList[position].score

        when (score) {
            1 -> binding.myReviewStar.setImageResource(R.drawable.one_star)
            2 -> binding.myReviewStar.setImageResource(R.drawable.two_star)
            3 -> binding.myReviewStar.setImageResource(R.drawable.three_star)
            4 -> binding.myReviewStar.setImageResource(R.drawable.four_star)
            5 -> binding.myReviewStar.setImageResource(R.drawable.five_star)
            else -> println("none")
        }
        binding.myReviewContent.text = myReviewList[position].review

        // 만약 picture가 ""이라면
        if ((myReviewList[position].picture).equals("")) {
            binding.myReviewPicture.visibility = View.GONE
        } else {
            Glide.with(binding.root.context).load(myReviewList[position].picture)
                .into(binding.myReviewPicture)
        }
        binding.myReviewDelete.setOnClickListener {
            val documentId = myReviewList[position].documentId
            val imageUrl = myReviewList[position].picture

            // Firestore에서 문서 삭제
            if (documentId != null) {
                db.collection("review").document(documentId)
                    .delete()
                    .addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            Log.d("성공", "Firestore에서 문서 삭제 성공")

                            // Firebase Storage에서 이미지 삭제
                            if (!imageUrl.isNullOrEmpty()) {
                                val storage = FirebaseStorage.getInstance()
                                val storageRef = storage.reference
                                val imageRef: StorageReference = storageRef.child(imageUrl)

                                // 이미지가 존재하는지 확인
                                imageRef.metadata.addOnSuccessListener { metadata ->
                                    if (metadata != null && metadata.sizeBytes > 0) {
                                        imageRef.delete()
                                            .addOnSuccessListener {
                                                Log.d("성공", "Storage에서 이미지 삭제 성공")
                                            }
                                            .addOnFailureListener {
                                                Log.e("실패", "Storage에서 이미지 삭제 실패", it)
                                            }
                                    } else {
                                        // 이미지가 존재하지 않음
                                        Log.e("실패", "이미지 존재하지 않음.")
                                    }
                                }
                                    .addOnFailureListener { exception ->
                                        // metadata를 가져오는 데 실패한 경우
                                        Log.e("실패", "이미지 삭제 실패", exception)
                                    }
                            }
                            myReviewList.removeAt(position)
                            listener.onReviewDeleted()
                            notifyDataSetChanged()
                        } else {
                            Log.d("에러", "에러 발생")
                        }
                    }
                    .addOnFailureListener {
                        Log.d("에러", "Firestore에서 문서 삭제 에러")
                    }
            }
        }
    }

    class MyViewHolder(val binding: MyReviewListBinding) : RecyclerView.ViewHolder(binding.root) {}
}


