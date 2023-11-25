package com.android.hakssok

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.hakssok.databinding.MyReviewListBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MyReviewAdapter(private val myReviewList: ArrayList<MyReviewListLayout>) :

    RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>() {
    override fun getItemCount(): Int = myReviewList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            MyReviewListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_recycler_view, parent, false)
//        return MyViewHolder(view)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = Firebase.firestore
        val binding = holder.binding
        binding.storeName.text = myReviewList[position].storeName
        Log.d("myReviewList??",myReviewList[position].storeName.toString())
        val score = myReviewList[position].score
        Log.d("score??",myReviewList[position].score.toString())

        when (score) {
            "1" -> binding.myReviewStar.setImageResource(R.drawable.one_star)
            "2" -> binding.myReviewStar.setImageResource(R.drawable.two_star)
            "3" -> binding.myReviewStar.setImageResource(R.drawable.three_star)
            "4" -> binding.myReviewStar.setImageResource(R.drawable.four_star)
            "5" -> binding.myReviewStar.setImageResource(R.drawable.five_star)
            else -> println("none")
        }
        binding.myReviewContent.text = myReviewList[position].review

        // 만약 picture가 ""이라면
        if ((myReviewList[position].picture) == null) {
            Log.d("picture 없음!!", "여기 찍혀야 함!!")
            binding.myReviewPicture.visibility = View.GONE
        } else {
            // Firebase Storage 참조
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            // Firebase Database에서 이미지 URL을 가져오는 로직 (예시)
            val imageUrl = myReviewList[position].picture

            // Firebase Storage에서 이미지 다운로드 URL로 참조 생성
            val imageRef: StorageReference = storageRef.child(imageUrl!!)

            // Picasso를 사용하여 이미지 로딩 및 ImageView에 설정
            Picasso.get().load(imageRef.toString()).into(binding.myReviewPicture)
        }
        binding.myReviewDelete.setOnClickListener {
            // 삭제 버튼 클릭 시 해당 문서 삭제
            val documentId = myReviewList[position].documentId
            if (documentId != null) {
                db.collection("user").document(documentId)
                    .delete()
                    .addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            Log.d("삭제 성공!", "성공띠")
                            myReviewList.removeAt(position)
                            notifyDataSetChanged()
                        } else {
                            Log.d("에러 발생!", "에러..")
                        }
                    }
            } else Log.d("오잉 null일리가.. 발생!", "null 임..")
        }
    }

    class MyViewHolder(val binding: MyReviewListBinding) : RecyclerView.ViewHolder(binding.root) {
//        val name: TextView = itemView.findViewById(R.id.store_name)
//        val location: TextView = itemView.findViewById(R.id.my_review_star)
//        val location: TextView = itemView.findViewById(R.id.my_review_picture)
//        val coupon: RecyclerView = itemView.findViewById(R.id.my_review_content)
    }
}


//class MyReviewAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {
//    val fragments: List<Fragment>
//    init {
//        fragments = listOf(MyReviewFragment())
//    }
//    override fun getItemCount(): Int = fragments.size
//    override fun createFragment(position: Int): Fragment = fragments[position]
//}
//class MyViewHolder(val binding: MyReviewRecyclerBinding) : RecyclerView.ViewHolder(binding.root)
//
//class MyReviewAdapter(val datas: MutableList<String>) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    override fun getItemCount(): Int {
//        return datas.size
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
//            RecyclerView.ViewHolder = MyViewHolder(
//        MyReviewRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//    )
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
////        val images = intArrayOf(
////            R.drawable.apple, R.drawable.banana, R.drawable.blueberry,
////            R.drawable.grape, R.drawable.orange, R.drawable.raspberry, R.drawable.strawberry,
////            R.drawable.watermelon
////        )
//
//        val binding = (holder as MyViewHolder).binding
//        binding.rrr.text = datas[position]
//        binding.rrr.setBackgroundColor(Color.BLUE)
//    }
////        val keys = datas.keys.toList()
////        val keyAtIndex = keys[position] // "사과"
////        val value = datas[keyAtIndex]
////
////        binding.itemImageOne.setImageResource(images[position])
////        binding.itemDataOne1.text = keyAtIndex
////        binding.itemDataOne2.text = value
//}


