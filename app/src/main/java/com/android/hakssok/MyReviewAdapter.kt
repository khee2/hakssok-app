package com.android.hakssok

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.hakssok.databinding.MyReviewListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URLDecoder

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

        if ((myReviewList[position].picture).equals("")) {
            binding.myReviewPicture.visibility = View.GONE
        } else { // picture가 있는 경우
            Glide.with(binding.root.context).load(myReviewList[position].picture)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20))) // 사진 테두리
                .into(binding.myReviewPicture)
            // TODO 이미지 크기 크기에 맞게 화면에 출력하기 -> 실제 안드로이드 앱의 글 등록(이미지 등록)을 통해 이미지 직접 저장헤버고 결과 비교하기
        }
        binding.myReviewDelete.setOnClickListener {
            val documentId = myReviewList[position].documentId
            val imageUrl = myReviewList[position].picture

            // 확인 메시지 표시
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
            alertDialogBuilder
                .setMessage("리뷰를 삭제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    if (documentId != null) {
                        val db = Firebase.firestore
                        // Firestore에서 문서 삭제
                        db.collection("review").document(documentId)
                            .delete()
                            .addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    Log.d("성공", "Firestore에서 문서 삭제 성공")

                                    // Firebase Storage에서 이미지 삭제
                                    if (!imageUrl.isNullOrEmpty()) {

                                        // 이미지 이름 추출하기
                                        val startIndex = imageUrl.lastIndexOf('/') + 1
                                        val endIndex = imageUrl.indexOf('?')
                                        val extractedPart = imageUrl.substring(startIndex, endIndex)
                                        Log.d("extractedPart?!", extractedPart.toString())

                                        val decodedImageName =
                                            URLDecoder.decode(extractedPart, "UTF-8")
                                        Log.d("decodedImageName?!", decodedImageName.toString())

                                        val storage = FirebaseStorage.getInstance()
                                        val storageRef = storage.reference
                                        val imageRef: StorageReference =
                                            storageRef.child("$decodedImageName")

                                        // 이미지가 존재하는지 확인
                                        imageRef.metadata.addOnSuccessListener { metadata ->
                                            if (metadata != null && metadata.sizeBytes > 0) {
                                                imageRef.delete()
                                                    .addOnSuccessListener {
                                                        Log.d("성공", "Storage에서 이미지 삭제 성공")
                                                        Toast.makeText(
                                                            binding.root.context,
                                                            "리뷰가 삭제되었습니다.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
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
                .setNegativeButton("아니오") { _, _ -> }
                .show()
        }
    }

    class MyViewHolder(val binding: MyReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}


